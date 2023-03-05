
package com.example.news.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.model.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapters : RecyclerView.Adapter<NewsAdapters.ArticleViewHolder>() {

    inner class ArticleViewHolder(item:View) : RecyclerView.ViewHolder(item)


    private val diffUtilCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem ==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtilCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
         return ArticleViewHolder(
             LayoutInflater.from(parent.context).inflate(
                 R.layout.item_article_preview,parent,false
             )
         )
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

         val article=differ.currentList[position]
             holder.itemView.apply {
                Glide.with(this).load(article.urlToImage).into(ivArticleImage)
                tvSource.text=article.source.name
                tvTitle.text=article.title
                tvDescription.text=article.description
                tvPublishedAt.text=article.publishedAt
            }
        holder.itemView.ivArticleImage.setOnClickListener {
            onItemClickListener?.let {
                it(article)
            }
        }
    }
    private var onItemClickListener: ((Article) -> Unit)?=null
    fun setOnItemClickListener(listener :(Article) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
          return differ.currentList.size
    }


}