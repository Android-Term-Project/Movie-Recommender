package com.example.movierecommender.pipeline

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class Tokenizer(context: Context, vocabFile: String) {
    private val vocab: Map<String, Int>
    private val maxLength = 512 // 최대 시퀀스 길이 설정

    init {
        val inputStream = context.assets.open(vocabFile)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val vocabBuilder = mutableMapOf<String, Int>()
        reader.useLines { lines ->
            lines.forEachIndexed { index, line ->
                vocabBuilder[line] = index
            }
        }
        vocab = vocabBuilder
    }

    fun tokenize(text: String): IntArray {
        val tokens = text.split(" ").map { vocab.getOrDefault(it, 0) }
        return tokens.take(maxLength).toIntArray()
    }

    fun getAttentionMask(inputIds: IntArray): IntArray {
        return IntArray(inputIds.size) { 1 }
    }
}

