package com.muzafferus.wordlearner.ui.word

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muzafferus.wordlearner.MainApplication
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityNewWordBinding

class NewWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewWordBinding

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as MainApplication).wordRepository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_word)

        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            when {
                TextUtils.isEmpty(binding.editWord.text) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                binding.wordTypeGroup.indexOfChild(findViewById(binding.wordTypeGroup.checkedRadioButtonId)) == -1 -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                else -> {
                    val isEdit = intent.getBooleanExtra(EXTRA_EDIT, false)
                    val index: Int =
                        binding.wordTypeGroup.indexOfChild(findViewById(binding.wordTypeGroup.checkedRadioButtonId))
                    val word = binding.editWord.text.toString()
                    if (!isEdit) {
                        replyIntent.putExtra(EXTRA_REPLY, word)
                        replyIntent.putExtra(EXTRA_INDEX, index)
                        setResult(Activity.RESULT_OK, replyIntent)
                    } else {
                        val intent = Intent(this@NewWordActivity, WordActivity::class.java)
                        intent.putExtra(EXTRA_REPLY, word)
                        intent.putExtra(EXTRA_INDEX, index)
                        intent.putExtra(EXTRA_EDIT, true)

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        this@NewWordActivity.finish()
                    }
                }
            }
            finish()
        }

        checkEdit()
    }

    private fun checkEdit() {
        val word = intent.getStringExtra(EXTRA_REPLY) ?: ""
        val isEdit = intent.getBooleanExtra(EXTRA_EDIT, false)
        val index = intent.getIntExtra(EXTRA_INDEX, -1)
        if (word != "" && isEdit && index != -1) {
            binding.editWord.setText(word)
            binding.wordTypeGroup.check(
                when (index) {
                    WordActivity.NOT_WONT_LEARN -> R.id.not_wont_learn
                    WordActivity.WONT_LEARN -> R.id.wont_learn
                    WordActivity.LEARNED -> R.id.learned
                    else -> R.id.uncategorized //WordActivity.NOT_WONT_LEARN
                }
            )

            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                wordViewModel.delete(word)
                onBackPressed()
            }
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.muzafferus.wordlearner.REPLY"
        const val EXTRA_INDEX = "com.muzafferus.wordlearner.INDEX"
        const val EXTRA_EDIT = "com.muzafferus.wordlearner.EDIT"
    }
}
