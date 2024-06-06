package com.android_project.movie_recommender

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_project.movie_recommender.databinding.RecommenderActivityBinding
import com.android_project.movie_recommender.pipeline.BERTInference
import com.android_project.movie_recommender.pipeline.GenreSimilarity
import com.android_project.movie_recommender.pipeline.MovieDataLoader
import com.android_project.movie_recommender.pipeline.MovieData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlinx.coroutines.*

class RecommenderViewActivity : AppCompatActivity() {
    private lateinit var binding: RecommenderActivityBinding
    private lateinit var adapter: RecommenderAdapter

    private lateinit var bertInference: BERTInference
    private lateinit var genreSimilarity: GenreSimilarity
    private lateinit var movies: List<MovieData>
    private lateinit var movieDatas: MutableList<OneMovieResponse>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecommenderActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bertInference = BERTInference(this)
        val movieDataLoader = MovieDataLoader(this)
        movies = movieDataLoader.loadMovies()
        genreSimilarity = GenreSimilarity(movies)

        val data: Movie = intent.getParcelableExtra("data")!!
        val btnReturn: Button = findViewById(R.id.rcmBtnReturn)
        movieDatas = mutableListOf()

        val inputMovie = movies.find { it.id == data.id }

        if (inputMovie != null) {
            val inputGenreId = inputMovie.genres
            val inputOverview = inputMovie.overview

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val inputOverviewEmbedding = withContext(Dispatchers.IO) {
                        // 입력 영화의 임베딩 벡터 계산
                        bertInference.getEmbedding(inputOverview)
                    }

                    val top10Indices = withContext(Dispatchers.IO) {
                        // 장르 기반 코사인 유사도로 상위 10개 영화 선택
                        genreSimilarity.getTopNIndices(inputGenreId, data.id, 10)
                    }

                    val similarities = withContext(Dispatchers.IO) {
                        val top10Movies = top10Indices.map { movies[it] }
                        top10Movies.map { movie ->
                            val similarity = bertInference.getSimilarityWithEmbedding(inputOverviewEmbedding, movie.overview)
                            Pair(movie, similarity)
                        }.sortedByDescending { it.second }.take(5)
                    }

                    val top5Movies = similarities.sortedByDescending { it.second }.take(5).map { it.first }
                    fetchApiData(top5Movies)

                    for ((movie, similarity) in similarities) {
                        Log.d("RecommenderViewActivity", "Movie: ${movie.title}, Similarity: $similarity")
                    }
                } catch (e: Exception) {
                    Log.e("RecommenderViewActivity", "Error processing movie data", e)
                }
           }
        } else {
            Log.d("RecommenderViewActivity", "Movie with ID ${data.id} not found.")
        }
        
        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun fetchApiData(top5Movies : List<MovieData> ){
        for(i in 0 .. 4){
            val api_key = "baa012ef8e89d16afdf7a0ad280fc6c2"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/${top5Movies[i].id}?api_key=${api_key}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("RecommenderViewActivity", "API Call failed", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d("RecommenderViewActivity API", responseBody ?: "No response body")  // 로그 추가
                        responseBody?.let {
                            val movieData = parseMovieData(it)
                            runOnUiThread {
                                movieDatas.add(movieData)
                                adapter.notifyDataSetChanged()  // Notify the adapter to refresh views
                                Log.d("RecommenderViewActivity API", "${movieData.title}")  // 로그 추가
                            }
                        }
                    } else {
                        Log.e("RecommenderViewActivity", "Server returned error: ${response.code}")
                    }
                }
            })
        }
        setupRecyclerView(movieDatas)
    }

    private fun parseMovieData(jsonResponse: String): OneMovieResponse {
        val gson = Gson()
        val movieResponse = gson.fromJson(jsonResponse, OneMovieResponse::class.java)
        return movieResponse
    }

    private fun setupRecyclerView(movieDatas: MutableList<OneMovieResponse>) {
        adapter = RecommenderAdapter(movieDatas)
        binding.RecommenderView.layoutManager = LinearLayoutManager(this)
        binding.RecommenderView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.RecommenderView.adapter = adapter
    }
}
