package com.codepath.recyclerviewlab

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.recyclerviewlab.R.layout
import com.codepath.recyclerviewlab.models.Article
import com.codepath.recyclerviewlab.networking.CallbackResponse
import com.codepath.recyclerviewlab.networking.NYTimesApiClient



class ArticleResultFragment: Fragment() {
    private val client = NYTimesApiClient()
    private val adapter = ArticleResultsRecyclerViewAdapter()

    private lateinit var list: RecyclerView
    private lateinit var progressSpinner: ContentLoadingProgressBar


    private var currentQuery: String? = null


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_search).actionView as SearchView
        item.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                currentQuery = query
                loadNewArticlesByQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_article_result_list, container, false)

        list = view.findViewById(R.id.list)
        progressSpinner= view.findViewById(R.id.progress)

        val linearLayoutManager = LinearLayoutManager(view.context)
        list.layoutManager = linearLayoutManager
        list.adapter = adapter

        //adding Endless View
        list.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadArticlesByPage(page)
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun loadNewArticlesByQuery(query: String) {
        Log.d("ArticleResultFragment", "loading articles for query $query")
        Toast.makeText(context, "Loading articles for \'$query\'", Toast.LENGTH_SHORT).show()

        progressSpinner.show()

        client.getArticlesByQuery(
            articlesListResponse = object : CallbackResponse<List<Article>> {
                override fun onSuccess(model: List<Article>) {
                    Log.d(ArticleResultFragment::class.java.simpleName, "Success")
                    adapter.setNewArticles(newArticles = model)
                    adapter.notifyDataSetChanged()

                    progressSpinner.hide()
                }

                override fun onFailure(error: Throwable?) {
                    Toast.makeText(context, error?.message, Toast.LENGTH_SHORT).show()
                    Log.d(ArticleResultFragment::class.java.simpleName, "failure")
                }
            },
            query = query
        )
    }

    private fun loadArticlesByPage(page: Int) {
        client.getArticlesByQuery(
            articlesListResponse = object : CallbackResponse<List<Article>> {
                override fun onSuccess(model: List<Article>) {
                    Log.d(
                        ArticleResultFragment::class.java.simpleName,
                        "Success: page - $page for $currentQuery"
                    )
                    adapter.addArticles(newArticles = model)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(error: Throwable?) {
                    Toast.makeText(context, error?.message, Toast.LENGTH_SHORT).show()
                    Log.d(
                        ArticleResultFragment::class.java.simpleName,
                        "failure to load page: $page"
                    )
                }
            },
            query = currentQuery,
            pageNumber = page)
    }
}