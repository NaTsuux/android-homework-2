package com.example.doggysim

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.doggysim.network.RequestApi
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

    private fun handleDoggy() {
        val testText = findViewById<TextView>(R.id.test_text)
        val testPic = findViewById<ImageView>(R.id.test_gif)
        launch(exceptionHandler) {
            val response = RequestApi.retrofitService.randDoggy()

            if (response.results != null) {
                val doggyName = response.results.doggyName.substringBeforeLast('.')
                val doggyComment = response.results.comment
                testText.text = doggyComment

                val doggyFile = File("$filesDir/$doggyName")

                if (!doggyFile.exists()) {
                    val result = RequestApi.retrofitService.getDoggy(doggyName)
                    result.byteStream().buffered().copyTo(doggyFile.outputStream())
                }
                testPic.setImageURI(Uri.fromFile(doggyFile))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkFileDir()

        val testButton = findViewById<Button>(R.id.test_button)

        testButton.setOnClickListener { handleDoggy() }

        SensorManagerHelper(this).setOnShakeListener(object : SensorManagerHelper.OnShakeListener {
            override fun onShake() {
                handleDoggy()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.cancel()
    }
}
