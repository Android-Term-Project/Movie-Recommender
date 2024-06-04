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

//        val safeOverview = if (datas[position].overview.length > 50) datas[position].overview.substring(0, 50) + "..." else datas[position].overview
        val movieStar = convertRatingToString(datas[position].vote_average)
        val movieVoteAvg = String.format("%.1f", datas[position].vote_average / 2.0)
//        binding.itemData.text = "제목: ${datas[position].title}\n별점: ${movieStar}(${movieVoteAvg})\n시놉시스: ${safeOverview}"
        val moviePopularity = datas[position].popularity

        binding.itemDataTitle.text = "${datas[position].title}"
        binding.itemDataRate.text = convertStarToYellow("별점: ${movieStar}(${movieVoteAvg})", movieStar)
        binding.itemDataPopularity.text = "인기도: ${moviePopularity}"

        binding.itemRoot.setOnClickListener {
            Log.d("recyclerViewItem", "item root click : $position")
        }

        toSubActivity.setOnClickListener {
            val data: Movie = datas[position]
            val context = holder.itemView.context
            val intent = Intent(context, SubActivity::class.java)
            intent.putExtra("data", data)
            context.startActivity(intent)
        }
    }
    private fun convertStarToYellow(fullText : String, movieStar : String): SpannableString{
        val spannable = SpannableString(fullText)
        val start = fullText.indexOf(movieStar)
        val end = start + movieStar.length
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
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
