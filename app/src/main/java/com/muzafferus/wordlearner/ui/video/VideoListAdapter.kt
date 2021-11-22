package com.muzafferus.wordlearner.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muzafferus.wordlearner.databinding.ListItemVideoBinding
import com.muzafferus.wordlearner.model.VideoModel

class VideoListAdapter(private val click: (VideoModel) -> Unit) :
    ListAdapter<VideoModel, VideoListAdapter.VideoViewHolder>(VideosComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position), click)

    }

    class VideoViewHolder(private val binding: ListItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoModel, click: (VideoModel) -> Unit) {
            binding.main.setOnClickListener { click.invoke(video) }
            binding.name.text = video.name
            binding.transcript.text = video.transcript
            binding.url.text = video.url
            binding.percent.text = String.format("%s %d%%", "Percent:", video.percent)
        }

        companion object {
            fun from(parent: ViewGroup): VideoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemVideoBinding.inflate(layoutInflater, parent, false)

                return VideoViewHolder(binding)
            }
        }
    }

    class VideosComparator : DiffUtil.ItemCallback<VideoModel>() {
        override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
            return oldItem.name == newItem.name
        }
    }
}