package com.example.practice1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_activity)

        val data : Movie = intent.getParcelableExtra("data")!!
        val movie_title: TextView = findViewById<TextView>(R.id.movie_title)
        val movie_overview: TextView = findViewById<TextView>(R.id.movie_overview)
        val movie_image: ImageView = findViewById(R.id.movie_image)
        val btnReturn : Button = findViewById(R.id.btnReturn)
        val btnRecommender : Button = findViewById(R.id.btnRecommender)

        movie_title.text = data.title
        movie_overview.text = data.overview

        val imageUrl = "https://image.tmdb.org/t/p/original/${data.poster_path}"
        Glide.with(this)
            .load(imageUrl)
            .into(movie_image)

        btnReturn.setOnClickListener {
            finish()
        }

        btnRecommender.setOnClickListener {
            val intent = Intent(this, RecommenderViewActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }
}