package com.example.movierecommender.pipeline

import android.content.Context
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BERTModel(context: Context, modelFilePath: String) {
    private var module: Module = LiteModuleLoader.load(assetFilePath(context, modelFilePath))

    @Throws(IOException::class)
    private fun assetFilePath(context: Context, assetName: String): String {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }

        context.assets.open(assetName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
        }
        return file.absolutePath
    }

    fun predict(inputIds: IntArray, attentionMask: IntArray): FloatArray {
        // 입력 데이터 크기 확인
        if (inputIds.size > 512 || attentionMask.size > 512) {
            throw IllegalArgumentException("Input IDs and Attention Mask must have a maximum length of 512")
        }

        val inputIdsTensor = Tensor.fromBlob(inputIds.map { it.toLong() }.toLongArray(), longArrayOf(1, inputIds.size.toLong()))
        val attentionMaskTensor = Tensor.fromBlob(attentionMask.map { it.toLong() }.toLongArray(), longArrayOf(1, attentionMask.size.toLong()))

        val outputTensor = module.forward(IValue.from(inputIdsTensor), IValue.from(attentionMaskTensor)).toTensor()
        return outputTensor.dataAsFloatArray
    }
}
