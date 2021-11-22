package com.muzafferus.wordlearner.ui.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muzafferus.wordlearner.databinding.ListItemWordBinding
import com.muzafferus.wordlearner.model.WordModel

class WordListAdapter(private val click: (WordModel) -> Unit) :
    ListAdapter<WordModel, WordListAdapter.WordViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, click)
    }

    class WordViewHolder(private val binding: ListItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(wordModel: WordModel, click: (WordModel) -> Unit) {
            binding.textView.text = wordModel.word
            binding.main.setOnClickListener { click.invoke(wordModel) }
        }

        companion object {
            fun from(parent: ViewGroup): WordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWordBinding.inflate(layoutInflater, parent, false)

                return WordViewHolder(binding)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<WordModel>() {
        override fun areItemsTheSame(oldItem: WordModel, newItem: WordModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WordModel, newItem: WordModel): Boolean {
            return oldItem.word == newItem.word
        }
    }
}