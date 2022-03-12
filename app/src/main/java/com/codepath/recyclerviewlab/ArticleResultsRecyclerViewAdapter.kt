package com.codepath.recyclerviewlab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codepath.recyclerviewlab.models.Article

// Define a static int for each view type, loading = showing the loading spinner at the end of the list
const val VIEW_TYPE_LOADING = 0
const val VIEW_TYPE_ARTICLE = 1

class ArticleResultsRecyclerViewAdapter : RecyclerView.Adapter<ArticleResultsRecyclerViewAdapter.ArticleViewHolder>(){

    private val articles = mutableListOf<Article>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleResultsRecyclerViewAdapter.ArticleViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.fragment_article_result, parent, false)

        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ArticleResultsRecyclerViewAdapter.ArticleViewHolder,
        position: Int
    ) {
        val article: Article = articles.get(position)

        holder.article_pub_date.text = article.publishDate
        holder.article_headline.text = article.headline?.main
        holder.article_snippet.text = article.snippet
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setNewArticles(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
    }

    fun addArticles(newArticles: List<Article>) {
        articles.addAll(newArticles)
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val article_pub_date = itemView.findViewById<TextView>(R.id.article_pub_date)
        val article_headline = itemView.findViewById<TextView>(R.id.article_headline)
        val article_snippet = itemView.findViewById<TextView>(R.id.article_snippet)

    }
}