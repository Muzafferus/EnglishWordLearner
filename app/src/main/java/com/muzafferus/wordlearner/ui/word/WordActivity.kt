package com.muzafferus.wordlearner.ui.word

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.muzafferus.wordlearner.MainApplication
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityWordBinding
import com.muzafferus.wordlearner.model.WordModel
import com.muzafferus.wordlearner.typesdef.WordTypes
import com.muzafferus.wordlearner.ui.article.NewArticleActivity

class WordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordBinding
    private val newWordActivityRequestCode = 1

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as MainApplication).wordRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_word)
        initAdapter()
        initFab()
        checkEdit()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.wordRecyclerView.layoutManager = linearLayoutManager

        val adapter = WordListAdapter(object : (WordModel) -> Unit {
            override fun invoke(word: WordModel) {
                val intent = Intent(this@WordActivity, NewWordActivity::class.java)
                intent.putExtra(NewWordActivity.EXTRA_REPLY, word.word)
                intent.putExtra(NewWordActivity.EXTRA_INDEX, word.type)
                intent.putExtra(NewArticleActivity.EXTRA_ID, word.id)
                startActivity(intent)
            }
        })
        binding.wordRecyclerView.adapter = adapter

        wordViewModel.allWords.observe(this, { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })

    }

    private fun initFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@WordActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    private fun checkEdit() {
        val word = intent.getStringExtra(NewWordActivity.EXTRA_REPLY) ?: ""
        val id = intent.getLongExtra(NewWordActivity.EXTRA_ID, -1)
        val index = intent.getIntExtra(NewWordActivity.EXTRA_INDEX, -1)

        if (word != "" && id != -1L && index != -1) {
            wordViewModel.update(
                WordModel(
                    id, word, when (index) {
                        NOT_WONT_LEARN -> WordTypes.NOT_WONT_LEARN
                        WONT_LEARN -> WordTypes.WONT_LEARN
                        LEARNED -> WordTypes.LEARNED
                        else -> WordTypes.UNCATEGORIZED //UNCATEGORIZED
                    }
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let { reply ->
                val word = WordModel(
                    null, reply, when (intentData.getIntExtra(NewWordActivity.EXTRA_INDEX, -1)) {
                        NOT_WONT_LEARN -> WordTypes.NOT_WONT_LEARN
                        WONT_LEARN -> WordTypes.WONT_LEARN
                        LEARNED -> WordTypes.LEARNED
                        else -> WordTypes.UNCATEGORIZED //UNCATEGORIZED
                    }
                )
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val UNCATEGORIZED = 0
        const val NOT_WONT_LEARN = 1
        const val WONT_LEARN = 2
        const val LEARNED = 3
    }
}
