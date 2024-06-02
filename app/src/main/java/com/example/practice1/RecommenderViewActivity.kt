package com.example.practice1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.practice1.pipeline.BERTInference
import com.example.practice1.pipeline.GenreSimilarity
import com.example.practice1.pipeline.MovieDataLoader
import com.example.practice1.pipeline.MovieData

class RecommenderViewActivity : AppCompatActivity() {
    private lateinit var bertInference: BERTInference
    private lateinit var genreSimilarity: GenreSimilarity
    private lateinit var movies: List<MovieData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommender_activity)

        bertInference = BERTInference(this)
        val movieDataLoader = MovieDataLoader(this)
        movies = movieDataLoader.loadMovies()
        genreSimilarity = GenreSimilarity(movies)

        val data : Movie = intent.getParcelableExtra("data")!!
        val btnReturn : Button = findViewById(R.id.btnRcmReturn)
        val movie_title: TextView = findViewById<TextView>(R.id.rcm_movie_title)

        movie_title.text = data.title
        val inputMovie = movies.find { it.id == data.id }

        if (inputMovie != null) {
            val inputGenreId = inputMovie.genres
            val inputOverview = inputMovie.overview

            try {
                // 입력 영화의 임베딩 벡터 계산
                val inputOverviewEmbedding = bertInference.getEmbedding(inputOverview)

                // 1. 장르 기반 코사인 유사도로 상위 10개 영화 선택
                val top10Indices = genreSimilarity.getTopNIndices(inputGenreId, data.id, 10)
                val top10Movies = top10Indices.map { movies[it] }

                val similarities = mutableListOf<Pair<MovieData, Float>>()
                for (movie in top10Movies) {
                    val similarity = bertInference.getSimilarityWithEmbedding(inputOverviewEmbedding, movie.overview)
                    similarities.add(Pair(movie, similarity))
                }

                // 유사도 기준 상위 5개 영화 선택
                val top5Movies = similarities.sortedByDescending { it.second }.take(5)
                for ((movie, similarity) in top5Movies) {
                    Log.d("MainActivity", "Movie: ${movie.title}, Similarity: $similarity")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error processing movie data", e)
            }
        } else {
            Log.d("MainActivity", "Movie with ID ${data.id} not found.")
        }

        btnReturn.setOnClickListener {
            finish()
        }

    }
}
