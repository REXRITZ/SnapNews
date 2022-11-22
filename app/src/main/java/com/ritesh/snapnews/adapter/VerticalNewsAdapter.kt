package com.ritesh.snapnews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ritesh.snapnews.databinding.NewsDetailItemBinding
import com.ritesh.snapnews.model.News

class VerticalNewsAdapter:  ListAdapter<News, VerticalNewsAdapter.NewsViewHolder>(NewsComparator()) {

    private var onDisplayWebViewClickListener: ((String) -> Unit)? = null

    fun setDisplayWebViewClickListener(listener: (String) -> Unit) {
        onDisplayWebViewClickListener = listener
    }

    inner class NewsViewHolder(val binding: NewsDetailItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(news: News) {
            binding.title.text = news.title
            binding.description.text = news.description
            Glide.with(binding.thumbnail.context)
                .load(news.imageUrl)
                .into(binding.thumbnail)

            binding.webViewBtn.setOnClickListener { view ->
                onDisplayWebViewClickListener?.let { it -> it(news.newsUrl) }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalNewsAdapter.NewsViewHolder {
        val binding = NewsDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}
