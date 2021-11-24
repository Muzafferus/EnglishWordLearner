package com.muzafferus.wordlearner.ui.article

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.muzafferus.articlelearner.ui.article.ArticleListAdapter
import com.muzafferus.wordlearner.MainApplication
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityArticlesBinding
import com.muzafferus.wordlearner.model.ArticleModel
import com.muzafferus.wordlearner.model.WordModel
import com.muzafferus.wordlearner.typesdef.WordTypes
import com.muzafferus.wordlearner.ui.read.ReadActivity
import com.muzafferus.wordlearner.ui.word.WordViewModel
import com.muzafferus.wordlearner.ui.word.WordViewModelFactory

class ArticlesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticlesBinding
    private val newArticleActivityRequestCode = 1

    private val articleViewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory((application as MainApplication).articleRepository)
    }

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as MainApplication).wordRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles)
        initAdapter()
        initFab()
        checkEdit()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.articleRecyclerView.layoutManager = linearLayoutManager

        val adapter = ArticleListAdapter(object : (ArticleModel) -> Unit {
            override fun invoke(article: ArticleModel) {
                val intent = Intent(this@ArticlesActivity, ReadActivity::class.java)
                intent.putExtra(NewArticleActivity.EXTRA_NAME, article.name)
                intent.putExtra(NewArticleActivity.EXTRA_DESCRIPTION, article.description)
                intent.putExtra(NewArticleActivity.EXTRA_ID, article.id)
                startActivity(intent)
            }
        })
        binding.articleRecyclerView.adapter = adapter

        wordViewModel.allWords.observe(this, { words ->
            articleViewModel.allArticles.observe(this, { articles ->
                // Update the cached copy of the articles in the adapter.
                articles?.let { adapter.submitList(articleViewModel.filteredList(it, words)) }
            })
        })
    }

    private fun checkEdit() {
        val name = intent.getStringExtra(NewArticleActivity.EXTRA_NAME) ?: ""
        val id = intent.getLongExtra(NewArticleActivity.EXTRA_ID, -1)
        val description = intent.getStringExtra(NewArticleActivity.EXTRA_DESCRIPTION) ?: ""

        if (name != "" && id != -1L && description != "") {
            val editedArticle = ArticleModel(id, name, description)
            articleViewModel.update(editedArticle)
        }
    }

    private fun initFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@ArticlesActivity, NewArticleActivity::class.java)
            startActivityForResult(intent, newArticleActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newArticleActivityRequestCode && resultCode == Activity.RESULT_OK) {

            var articleName = ""
            var articleDescription = ""
            intentData?.getStringExtra(NewArticleActivity.EXTRA_NAME)?.let { name ->
                articleName = name
            }
            intentData?.getStringExtra(NewArticleActivity.EXTRA_DESCRIPTION)?.let { description ->
                articleDescription = description
            }

            val article = ArticleModel(null, articleName, articleDescription)
            articleViewModel.insert(article)

            getWords(article).forEach {
                word -> wordViewModel.insert(WordModel( word.trim().lowercase(), WordTypes.UNCATEGORIZED))
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getWords(article: ArticleModel): ArrayList<String> {
       val wordList = articleViewModel.getWordList(article.description)
        return ArrayList(wordList)
    }
}