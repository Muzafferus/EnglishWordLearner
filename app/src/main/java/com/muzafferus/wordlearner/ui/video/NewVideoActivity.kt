package com.muzafferus.wordlearner.ui.video

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muzafferus.wordlearner.R
import com.muzafferus.wordlearner.databinding.ActivityNewVideoBinding

class NewVideoActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNewVideoBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_video)

        var name = intent.getStringExtra(EXTRA_NAME) ?: ""
        val id = intent.getLongExtra(EXTRA_ID, -1L)
        var transcript = intent.getStringExtra(EXTRA_TRANSCRIPT) ?: ""
        var url = intent.getStringExtra(EXTRA_URL) ?: ""
        if (name != "" && id != -1L && transcript != "" && url != "") {
            binding.editName.setText(name)
            binding.editTranscript.setText(transcript)
            binding.editUrl.setText(url)
        }

        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            when {
                TextUtils.isEmpty(binding.editName.text) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                TextUtils.isEmpty(binding.editTranscript.text) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                TextUtils.isEmpty(binding.editUrl.text) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
                else -> {
                    if (id == -1L) {
                        name = binding.editName.text.toString()
                        transcript = binding.editTranscript.text.toString()
                        url = binding.editUrl.text.toString()
                        replyIntent.putExtra(EXTRA_NAME, name)
                        replyIntent.putExtra(EXTRA_TRANSCRIPT, transcript)
                        replyIntent.putExtra(EXTRA_URL, url)
                        setResult(Activity.RESULT_OK, replyIntent)
                    } else {
                        name = binding.editName.text.toString()
                        transcript = binding.editTranscript.text.toString()
                        url = binding.editUrl.text.toString()
                        val intent = Intent(this@NewVideoActivity, VideoActivity::class.java)
                        intent.putExtra(EXTRA_NAME, name)
                        intent.putExtra(EXTRA_TRANSCRIPT, transcript)
                        intent.putExtra(EXTRA_URL, url)
                        intent.putExtra(EXTRA_ID, id)

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        this@NewVideoActivity.finish()
                    }

                }
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_NAME = "com.muzafferus.wordlearner.NAME"
        const val EXTRA_ID = "com.muzafferus.wordlearner.ID"
        const val EXTRA_TRANSCRIPT = "com.muzafferus.wordlearner.TRANSCRIPT"
        const val EXTRA_URL = "com.muzafferus.wordlearner.URL"
    }
}
