package com.example.practice1.pipeline

import android.content.Context

class BERTInference(context: Context) {
    private val bertModel: BERTModel = BERTModel(context, "distilbert_traced.ptl")
    private val tokenizer: Tokenizer = Tokenizer(context, "vocab.txt")

    fun getEmbedding(text: String): FloatArray {
        val inputIds = tokenizer.tokenize(text)
        val attentionMask = tokenizer.getAttentionMask(inputIds)
        return bertModel.predict(inputIds, attentionMask)
    }

    private fun cosineSimilarity(vec1: FloatArray, vec2: FloatArray): Float {
        val length = maxOf(vec1.size, vec2.size)

        val paddedVec1 = vec1 + FloatArray(length - vec1.size)
        val paddedVec2 = vec2 + FloatArray(length - vec2.size)

        var dotProduct = 0.0f
        var normVec1 = 0.0f
        var normVec2 = 0.0f
        for (i in 0 until length) {
            dotProduct += paddedVec1[i] * paddedVec2[i]
            normVec1 += paddedVec1[i] * paddedVec1[i]
            normVec2 += paddedVec2[i] * paddedVec2[i]
        }
        return dotProduct / (Math.sqrt(normVec1.toDouble()) * Math.sqrt(normVec2.toDouble())).toFloat()
    }

    fun getSimilarityWithEmbedding(embedding1: FloatArray, text2: String): Float {
        val embedding2 = getEmbedding(text2)
        return cosineSimilarity(embedding1, embedding2)
    }
}
