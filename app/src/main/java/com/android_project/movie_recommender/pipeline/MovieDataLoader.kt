package com.android_project.movie_recommender.pipeline


import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream

data class MovieData(val id: Int, val title: String, val genres: List<Int>, val overview: String)

class MovieDataLoader(private val context: Context) {
    fun loadMovies(): List<MovieData> {
        val json: String = try {
            val inputStream: InputStream = context.assets.open("tmdb5000_movies.json")
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return emptyList()
        }

        val movieType = object : TypeToken<List<MovieData>>() {}.type
        return Gson().fromJson(json, movieType)
    }
}
