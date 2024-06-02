package com.example.movierecommender.pipeline

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class GenreSimilarity(private val movies: List<Movie>) {
    private val genreMatrix: RealMatrix = buildGenreMatrix(movies)

    private fun buildGenreMatrix(movies: List<Movie>): RealMatrix {
        val rowCount = movies.size
        val colCount = movies.flatMap { it.genres }.distinct().maxOrNull()?.plus(1) ?: 0
        val matrix = MatrixUtils.createRealMatrix(rowCount, colCount)
        for ((rowIndex, movie) in movies.withIndex()) {
            for (genre in movie.genres) {
                matrix.setEntry(rowIndex, genre, 1.0)
            }
        }
        return matrix
    }

    fun getTopNIndices(inputGenres: List<Int>, inputMovieId: Int, n: Int): List<Int> {
        val inputVector = MatrixUtils.createRealVector(DoubleArray(genreMatrix.columnDimension) { 0.0 })
        for (genre in inputGenres) {
            inputVector.setEntry(genre, 1.0)
        }

        val similarities = genreMatrix.operate(inputVector)
        val sortedIndices = similarities.toArray()
            .withIndex()
            .filter { movies[it.index].id != inputMovieId } // 입력 영화 ID를 제외
            .sortedByDescending { it.value }
            .map { it.index }

        return sortedIndices.take(n)
    }
}


