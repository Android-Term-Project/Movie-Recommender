package com.android_project.movie_recommender

import android.content.Intent
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(private val datas: MutableList<Movie>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MyViewHolder(
        ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("recyclerViewItem", "onBindViewGolder : $position")
        val binding = (holder as MyViewHolder).binding
        val toSubActivity = binding.itemRoot

        val imageUrl = "https://image.tmdb.org/t/p/original/${datas[position].poster_path}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(binding.itemImage)

        val movieStar = convertRatingToString(datas[position].vote_average)
        val movieVoteAvg = String.format("%.1f", datas[position].vote_average / 2.0)
        val movieReleaseDate = convertReleaseDate(datas[position].release_date)

        binding.itemDataTitle.text = "${datas[position].title}"
        binding.itemDataRate.text = convertStarToGrey("별점: ${movieStar}(${movieVoteAvg})", movieStar)
        binding.itemDataReleaseDate.text = "개봉일: ${movieReleaseDate}"
        val dataMovieVote = binding.itemDataRate.text

        binding.itemRoot.setOnClickListener {
            Log.d("recyclerViewItem", "item root click : $position")
        }

        toSubActivity.setOnClickListener {
            val data: Movie = datas[position]
            val context = holder.itemView.context
            val intent = Intent(context, SubActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("dataMovieVote", dataMovieVote)
            context.startActivity(intent)
        }
    }
    private fun convertStarToGrey(fullText : String, movieStar : String): SpannableString{
        val spannable = SpannableString(fullText)
        val start = fullText.indexOf(movieStar)
        val end = start + movieStar.length
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    fun convertReleaseDate(releaseDate: String): String {
        // ISO_LOCAL_DATE 형식의 문자열을 LocalDate 객체로 파싱
        val date = LocalDate.parse(releaseDate)
        // 원하는 출력 형식을 정의
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        // 변환된 날짜를 지정된 형식으로 포맷
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

    fun addMovies(newMovies: List<Movie>) {
        val startPosition = datas.size
        datas.addAll(newMovies)
        notifyItemRangeInserted(startPosition, newMovies.size)
    }
}
