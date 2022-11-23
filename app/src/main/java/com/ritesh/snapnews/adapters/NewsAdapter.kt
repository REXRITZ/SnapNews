package com.ritesh.snapnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.ritesh.snapnews.R
import com.ritesh.snapnews.databinding.NewsItemBinding
import com.ritesh.snapnews.model.News
import com.ritesh.snapnews.util.Utils

class NewsAdapter: ListAdapter<News, NewsAdapter.NewsViewHolder>(NewsComparator()) {

    private var onNewsClickListener: ((Int) -> Unit)? = null

    fun setOnNewsClickListener(listener: (Int) -> Unit) {
        onNewsClickListener = listener
    }

    inner class NewsViewHolder(val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(news: News) {
            binding.title.text = news.title
            binding.info.text = "${Utils.getRelativeDateTime(news.publishAt)} ∙ ${news.readTime}"
            Glide.with(binding.thumbnail.context)
                .load(news.imageUrl)
                .into(binding.thumbnail)

            binding.root.setOnClickListener {
                onNewsClickListener?.let { it(adapterPosition) }
            }
        }
    }

    class NewsComparator: DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}