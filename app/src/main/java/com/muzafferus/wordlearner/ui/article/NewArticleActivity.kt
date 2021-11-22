package com.muzafferus.wordlearner.ui.article

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityNewArticleBinding

class NewArticleActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNewArticleBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_article)

        var name = intent.getStringExtra(EXTRA_NAME) ?: ""
        val id = intent.getLongExtra(EXTRA_ID, -1L)
        var description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""
        if (name != "" && id != -1L && description != "") {
            binding.editName.setText(name)
            binding.editDescription.setText(description)
        }

        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            when {
                TextUtils.isEmpty(binding.editName.text) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                TextUtils.isEmpty(binding.editDescription.text) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                else -> {
                    if(id == -1L){
                        name = binding.editName.text.toString()
                        description = binding.editDescription.text.toString()
                        replyIntent.putExtra(EXTRA_NAME, name)
                        replyIntent.putExtra(EXTRA_DESCRIPTION, description)
                        setResult(Activity.RESULT_OK, replyIntent)
                    }else{
                        name = binding.editName.text.toString()
                        description = binding.editDescription.text.toString()
                        val intent = Intent(this@NewArticleActivity, ArticlesActivity::class.java)
                        intent.putExtra(EXTRA_NAME, name)
                        intent.putExtra(EXTRA_DESCRIPTION, description)
                        intent.putExtra(EXTRA_ID, id)

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        this@NewArticleActivity.finish()
                    }

                }
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_NAME = "com.muzafferus.wordlearner.NAME"
        const val EXTRA_ID = "com.muzafferus.wordlearner.ID"
        const val EXTRA_DESCRIPTION = "com.muzafferus.wordlearner.DESCRIPTION"
    }
}
