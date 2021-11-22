package com.muzafferus.wordlearner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muzafferus.wordlearner.databinding.ActivityMainBinding
import com.muzafferus.wordlearner.ui.article.ArticlesActivity
import com.muzafferus.wordlearner.ui.word.WordActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }

    private fun initView() {
        binding.buttonWord.setOnClickListener {
            startActivity(Intent(this, WordActivity::class.java))
        }
        binding.buttonArticle.setOnClickListener {
            startActivity(Intent(this, ArticlesActivity::class.java))
        }
    }
}