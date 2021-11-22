package com.muzafferus.wordlearner.ui.read

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muzafferus.wordlearner.ui.article.ArticleViewModel
import com.muzafferus.wordlearner.ui.article.ArticleViewModelFactory
import com.muzafferus.wordlearner.MainApplication
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityReadBinding
import com.muzafferus.wordlearner.ui.article.NewArticleActivity

class ReadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadBinding

    private val articleViewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory((application as MainApplication).articleRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_read)

        val name = intent.getStringExtra(NewArticleActivity.EXTRA_NAME) ?: ""
        val id = intent.getLongExtra(NewArticleActivity.EXTRA_ID, -1)
        val description = intent.getStringExtra(NewArticleActivity.EXTRA_DESCRIPTION) ?: ""

        binding.name.text = name
        binding.description.text = description


        binding.delete.setOnClickListener {
            articleViewModel.delete(id)
            onBackPressed()
        }
        binding.edit.setOnClickListener {
            val intent = Intent(this@ReadActivity, NewArticleActivity::class.java)
            intent.putExtra(NewArticleActivity.EXTRA_NAME, name)
            intent.putExtra(NewArticleActivity.EXTRA_DESCRIPTION, description)
            intent.putExtra(NewArticleActivity.EXTRA_ID, id)
            startActivity(intent)
        }
    }
}