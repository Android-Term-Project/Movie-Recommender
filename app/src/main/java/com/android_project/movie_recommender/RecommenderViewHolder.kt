package com.android_project.movie_recommender

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.android_project.movie_recommender.databinding.ItemMainBinding
import com.android_project.movie_recommender.pipeline.MovieData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecommenderViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class RecommenderAdapter(private val movieDatas: MutableList<OneMovieResponse>): RecyclerView.Adapter<RecommenderViewHolder>() {
    override fun getItemCount(): Int = movieDatas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommenderViewHolder =
        RecommenderViewHolder(
            ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecommenderViewHolder, position: Int) {
        Log.d("RecommenderViewHolder", "onBindViewGolder : $position")
        val binding = holder.binding

        if (movieDatas.size > position) {
            binding.itemDataTitle.text = movieDatas[position].title
            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/original/${movieDatas[position].poster_path}")
                .into(binding.itemImage)
            val movieStar = movieDatas[position].vote_average?.let { convertRatingToString(it) } ?: "☆☆☆☆☆"
            val movieVoteAvg = movieDatas[position].vote_average?.let { String.format("%.1f", it / 2.0) } ?: "0.0"
            val movieReleaseDate = movieDatas[position].release_date?.let { convertReleaseDate(it) } ?: "날짜 정보 없음"
            binding.itemDataRate.text = convertStarToGrey("별점: ${movieStar}(${movieVoteAvg})", movieStar)
            binding.itemDataReleaseDate.text = "개봉일: ${movieReleaseDate}"
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_launcher_foreground)
                .into(binding.itemImage)
        }

    }

    private fun convertStarToGrey(fullText : String, movieStar : String): SpannableString {
        val spannable = SpannableString(fullText)
        val start = fullText.indexOf(movieStar)
        val end = start + movieStar.length
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    fun convertReleaseDate(releaseDate: String): String {
        val date = LocalDate.parse(releaseDate)
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        return date.format(formatter)
    }


    private fun convertRatingToString(rating: Double): String {
        val fullStars = (rating / 2).toInt()
        val isOneMore: Boolean = (rating / 2.0 - fullStars) >= 0.5
        val result = StringBuilder()
        if (isOneMore)
            result.append("★")
        for (i in 0 until fullStars)
            result.append("★")
        for (i in 0 until 5 - result.length)
            result.append("☆")

        return result.toString()
    }
}
