package com.example.practice1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice1.databinding.ItemMainBinding
import com.example.practice1.pipeline.MovieData // Importing MovieData if this is the object used in the adapter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RecommenderViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class RecommenderAdapter(private val datas: MutableList<MovieData>, private val imageUrls: MutableList<String>): RecyclerView.Adapter<RecommenderViewHolder>() { // Specify the ViewHolder type for clarity and ensure MovieData is used if that is the intended type
    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommenderViewHolder =
        RecommenderViewHolder(
            ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecommenderViewHolder, position: Int) {
        Log.d("RecommenderViewHolder", "onBindViewGolder : $position")
        val movie = datas[position]
        val binding = holder.binding

        binding.itemData.text = movie.title

        if (imageUrls.size > position) {
            Glide.with(holder.itemView.context)
                .load(imageUrls[position])
                .into(binding.itemImage)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_launcher_foreground)
                .into(binding.itemImage)
        }

        binding.root.setOnClickListener {
            Log.d("RecommenderAdapter", "Clicked on movie: ${movie.title}")
        }
    }

    fun addImageUrls(newImageUrl: String) {
        this.imageUrls.add(newImageUrl)
        notifyItemInserted(this.imageUrls.size - 1)
    }
}
