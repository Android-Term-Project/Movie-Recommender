package com.android_project.movie_recommender

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_project.movie_recommender.databinding.RecyclerViewBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: RecyclerViewBinding
    private lateinit var adapter: MyAdapter
    private var isLoading = false
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchApiData(currentPage)
    }

    private fun setupRecyclerView() {
        adapter = MyAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && currentPage <= totalPages) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        currentPage++
                        fetchApiData(currentPage)
                    }
                }
            }
        })
    }

    private fun fetchApiData(page: Int) {
        isLoading = true
        val api_key = "baa012ef8e89d16afdf7a0ad280fc6c2"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular?page=${page}&api_key=${api_key}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RecyclerViewActivity", "API Call failed", e)
                isLoading = false
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val (movies, total_pages) = parseResponse(it)
                        totalPages = total_pages
                        runOnUiThread {
                            adapter.addMovies(movies)
                        }
                    }
                } else {
                    Log.e("RecyclerViewActivity", "Server returned error: ${response.code}")
                }
                isLoading = false
            }
        })
    }

    private fun parseResponse(jsonResponse: String): Pair<List<Movie>, Int> {
        val gson = Gson()
        val movieResponse = gson.fromJson(jsonResponse, MovieResponse::class.java)
        return Pair(movieResponse.results, movieResponse.total_pages)
    }
}
