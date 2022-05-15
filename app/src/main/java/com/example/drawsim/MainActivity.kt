package com.example.drawsim

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.drawsim.network.RequestApi
import kotlinx.coroutines.*
import java.io.File


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(TAG, "Error: ${throwable.message}")
    }

    private fun checkFileDir() {
        if (!filesDir.exists()) {
            filesDir.mkdir()
            filesDir.setReadable(true)
            filesDir.setWritable(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkFileDir()

        val testButton = findViewById<Button>(R.id.test_button)
        val testText = findViewById<TextView>(R.id.test_text)
        val testPic = findViewById<ImageView>(R.id.test_pic)

        testButton.setOnClickListener {
            launch(exceptionHandler) {
                val tmp = RequestApi.retrofitService.getPhotos()
                val first = tmp[0].imgSrc.substringAfterLast('/')

                val avatar = File("$filesDir/$first")

                if (!avatar.exists()) {
                    val result = RequestApi.retrofitService.getPhoto(first)
                    result.byteStream().buffered().copyTo(avatar.outputStream())
                }
                testPic.setImageURI(Uri.fromFile(avatar))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.cancel()
    }
}
