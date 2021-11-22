package com.muzafferus.wordlearner.ui.read

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muzafferus.wordlearner.MainApplication
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityReadBinding
import com.muzafferus.wordlearner.ui.article.ArticleViewModel
import com.muzafferus.wordlearner.ui.article.ArticleViewModelFactory
import com.muzafferus.wordlearner.ui.article.NewArticleActivity
import com.muzafferus.wordlearner.ui.video.NewVideoActivity
import com.muzafferus.wordlearner.ui.video.VideoViewModel
import com.muzafferus.wordlearner.ui.video.VideoViewModelFactory

class ReadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadBinding

    private val articleViewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory((application as MainApplication).articleRepository)
    }
    private val videoViewModel: VideoViewModel by viewModels {
        VideoViewModelFactory((application as MainApplication).videoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_read)

        val name = intent.getStringExtra(NewArticleActivity.EXTRA_NAME) ?: ""
        val id = intent.getLongExtra(NewArticleActivity.EXTRA_ID, -1)
        val description = intent.getStringExtra(NewArticleActivity.EXTRA_DESCRIPTION) ?: ""
        val transcript = intent.getStringExtra(NewVideoActivity.EXTRA_TRANSCRIPT) ?: ""
        val url = intent.getStringExtra(NewVideoActivity.EXTRA_URL) ?: ""

        binding.name.text = name

        if (transcript == "" && url == "") {
            binding.description.text = description
        } else {
            binding.description.text = transcript
            binding.url.text = url
            binding.url.visibility = View.VISIBLE
        }

        binding.delete.setOnClickListener {
            if (transcript == "" && url == "") {
                articleViewModel.delete(id)
            } else {
                videoViewModel.delete(id)
            }
            onBackPressed()
        }
        binding.edit.setOnClickListener {
            if (transcript == "" && url == "") {
                val intent = Intent(this@ReadActivity, NewArticleActivity::class.java)
                intent.putExtra(NewArticleActivity.EXTRA_NAME, name)
                intent.putExtra(NewArticleActivity.EXTRA_DESCRIPTION, description)
                intent.putExtra(NewArticleActivity.EXTRA_ID, id)
                startActivity(intent)
            } else {
                val intent = Intent(this@ReadActivity, NewVideoActivity::class.java)
                intent.putExtra(NewVideoActivity.EXTRA_NAME, name)
                intent.putExtra(NewVideoActivity.EXTRA_TRANSCRIPT, transcript)
                intent.putExtra(NewVideoActivity.EXTRA_ID, id)
                intent.putExtra(NewVideoActivity.EXTRA_URL, url)
                startActivity(intent)
            }
        }
    }
}
