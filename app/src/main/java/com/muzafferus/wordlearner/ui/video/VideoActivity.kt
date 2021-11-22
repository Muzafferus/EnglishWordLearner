package com.muzafferus.wordlearner.ui.video

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
import com.muzafferus.wordlearner.databinding.ActivityVideosBinding
import com.muzafferus.wordlearner.model.VideoModel
import com.muzafferus.wordlearner.ui.read.ReadActivity
import com.muzafferus.wordlearner.ui.word.WordViewModel
import com.muzafferus.wordlearner.ui.word.WordViewModelFactory

class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideosBinding
    private val newVideoActivityRequestCode = 1

    private val videoViewModel: VideoViewModel by viewModels {
        VideoViewModelFactory((application as MainApplication).videoRepository)
    }

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as MainApplication).wordRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_videos)
        initAdapter()
        initFab()
        checkEdit()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.videoRecyclerView.layoutManager = linearLayoutManager

        val adapter = VideoListAdapter(object : (VideoModel) -> Unit {
            override fun invoke(video: VideoModel) {
                val intent = Intent(this@VideoActivity, ReadActivity::class.java)
                intent.putExtra(NewVideoActivity.EXTRA_NAME, video.name)
                intent.putExtra(NewVideoActivity.EXTRA_TRANSCRIPT, video.transcript)
                intent.putExtra(NewVideoActivity.EXTRA_ID, video.id)
                intent.putExtra(NewVideoActivity.EXTRA_URL, video.url)
                startActivity(intent)
            }
        })
        binding.videoRecyclerView.adapter = adapter

        wordViewModel.allWords.observe(this, { words ->
            videoViewModel.allVideos.observe(this, { videos ->
                // Update the cached copy of the videos in the adapter.
                videos?.let { adapter.submitList(videoViewModel.filteredList(it, words)) }
            })
        })
    }

    private fun checkEdit() {
        val name = intent.getStringExtra(NewVideoActivity.EXTRA_NAME) ?: ""
        val id = intent.getLongExtra(NewVideoActivity.EXTRA_ID, -1)
        val description = intent.getStringExtra(NewVideoActivity.EXTRA_TRANSCRIPT) ?: ""
        val url = intent.getStringExtra(NewVideoActivity.EXTRA_URL) ?: ""

        if (name != "" && id != -1L && description != "" && url != "") {
            val editedVideo = VideoModel(id, name, description, url)
            videoViewModel.update(editedVideo)
        }
    }

    private fun initFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@VideoActivity, NewVideoActivity::class.java)
            startActivityForResult(intent, newVideoActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newVideoActivityRequestCode && resultCode == Activity.RESULT_OK) {

            var videoName = ""
            var videoTranscript = ""
            var videoUrl = ""
            intentData?.getStringExtra(NewVideoActivity.EXTRA_NAME)?.let { name ->
                videoName = name
            }
            intentData?.getStringExtra(NewVideoActivity.EXTRA_TRANSCRIPT)?.let { description ->
                videoTranscript = description
            }
            intentData?.getStringExtra(NewVideoActivity.EXTRA_URL)?.let { description ->
                videoUrl = description
            }

            val video = VideoModel(null, videoName, videoTranscript, videoUrl)
            videoViewModel.insert(video)
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}