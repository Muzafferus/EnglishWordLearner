package com.muzafferus.articlelearner.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muzafferus.wordlearner.databinding.ListItemArticleBinding
import com.muzafferus.wordlearner.model.ArticleModel

class ArticleListAdapter(private val click: (ArticleModel) -> Unit) :
    ListAdapter<ArticleModel, ArticleListAdapter.ArticleViewHolder>(ArticlesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position), click)

    }

    class ArticleViewHolder(private val binding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleModel, click: (ArticleModel) -> Unit) {
            binding.main.setOnClickListener { click.invoke(article) }
            binding.name.text = article.name
            binding.description.text = article.description
            binding.percent.text = String.format("%s %d%%", "Percent:", article.percent)
        }

        companion object {
            fun from(parent: ViewGroup): ArticleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemArticleBinding.inflate(layoutInflater, parent, false)

                return ArticleViewHolder(binding)
            }
        }
    }

    class ArticlesComparator : DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.name == newItem.name
        }
    }
}