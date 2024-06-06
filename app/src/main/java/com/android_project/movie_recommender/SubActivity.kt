package com.android_project.movie_recommender

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android_project.movie_recommender.pipeline.MovieData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SubActivity : AppCompatActivity() {
    private lateinit var parsedData : OneMovieResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_activity)

        val data : Movie = intent.getParcelableExtra("data")!!
        fetchApiData(data.id)

        val btnReturn : Button = findViewById(R.id.btnReturn)
        val btnRecommender : Button = findViewById(R.id.btnRecommender)

        btnReturn.setOnClickListener {
            finish()
        }

        btnRecommender.setOnClickListener {
            val intent = Intent(this, RecommenderViewActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }

    private fun fetchApiData(id : Int){
        val api_key = "baa012ef8e89d16afdf7a0ad280fc6c2"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/${id}?api_key=${api_key}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SubActivity", "API Call failed", e)
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("SubActivity API", responseBody ?: "No response body")  // 로그 추가
                    responseBody?.let {
                        parsedData = parseResponse(it)
                        runOnUiThread {
                            updateUI(parsedData)
                        }
                    }
                } else {
                    Log.e("SubActivity", "Server returned error: ${response.code}")
                }
            }
        })
    }

    private fun updateUI(data: OneMovieResponse) {
        findViewById<TextView>(R.id.movie_title).text = data.title
        findViewById<TextView>(R.id.movie_overview).text = "줄거리:  ${data.overview}"
        val imageUrl = "https://image.tmdb.org/t/p/original/${data.poster_path}"
        Glide.with(this).load(imageUrl).into(findViewById<ImageView>(R.id.movie_image))
        findViewById<TextView>(R.id.movie_vote).text = intent.getCharSequenceExtra("dataMovieVote")
        findViewById<TextView>(R.id.movie_runtime).text = "상영시간:  ${data.runtime}분"
        findViewById<TextView>(R.id.movie_genres).text = getGenres(data.genres)
    }

    private fun parseResponse(jsonResponse: String): OneMovieResponse {
        val gson = Gson()
        val movieResponse = gson.fromJson(jsonResponse, OneMovieResponse::class.java)
        return movieResponse
    }

    private fun getGenres(genres: List<Genre>?): String {
        if (genres == null) {
            return "장르 데이터가 없습니다."
        }
        val genreString = genres.mapNotNull { it.name }.joinToString(separator = ", ", prefix = "장르:  ")
        return if (genreString == "장르:  ") "장르 데이터가 없습니다." else genreString
    }

}