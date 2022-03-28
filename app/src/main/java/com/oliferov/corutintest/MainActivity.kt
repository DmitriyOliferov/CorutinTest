package com.oliferov.corutintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isVisible
import com.oliferov.corutintest.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonDownload.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        binding.progress.isVisible = true
        binding.buttonDownload.isEnabled = false
        loadCity{
            binding.tvLocation.text =it
            loadTemperature(it){
                binding.tvTemperature.text = it.toString()
                binding.progress.isVisible = false
                binding.buttonDownload.isEnabled = true
            }
        }
    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Loading temperature for city: $city",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Thread.sleep(5000)
            runOnUiThread {
                callback(13)
            }

        }

    }

    private fun loadCity(callback: (String) -> Unit) {
        thread{
            Thread.sleep(5000)
            runOnUiThread {
                callback("Moscow")
            }
        }
    }
}