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
import kotlinx.coroutines.*

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

        val data: Movie = intent.getParcelableExtra("data")!!
        val btnReturn: Button = findViewById(R.id.btnRcmReturn)
        val movieTitle: TextView = findViewById<TextView>(R.id.rcm_movie_title)

        movieTitle.text = data.title
        btnReturn.setOnClickListener {
            finish()
        }

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

                    // UI 업데이트
                    similarities.forEach { (movie, similarity) ->
                        Log.d("RecommenderViewActivity", "Movie: ${movie.title}, Similarity: $similarity")
                    }

                } catch (e: Exception) {
                    Log.e("RecommenderViewActivity", "Error processing movie data", e)
                }
            }
        } else {
            Log.d("RecommenderViewActivity", "Movie with ID ${data.id} not found.")
        }


    }
}
