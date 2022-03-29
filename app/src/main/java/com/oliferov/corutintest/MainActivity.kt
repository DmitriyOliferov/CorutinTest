package com.oliferov.corutintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.oliferov.corutintest.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonDownload.setOnClickListener {
            lifecycleScope.launch {
                loadData()
            }
        }
    }

    private suspend fun loadData() {
        Log.i("MainActivity", "Load started $this")
        binding.progress.isVisible = true
        binding.buttonDownload.isEnabled = false
        val city = loadCity()
        binding.tvLocation.text = city
        val temp = loadTemperature(city)
        binding.tvTemperature.text = temp.toString()
        binding.progress.isVisible = false
        binding.buttonDownload.isEnabled = true
        Log.i("MainActivity", "Load finished $this")
    }

    private fun loadWithoutCoroutine(step: Int = 0, objects: Any? = null) {
        when (step) {
            0 -> {
                Log.i("MainActivity", "Load started $this")
                binding.progress.isVisible = true
                binding.buttonDownload.isEnabled = false
                loadCityWithoutCoroutine {
                    loadWithoutCoroutine(1, it)
                }
            }
            1 -> {
                val city = objects as String
                binding.tvLocation.text = city
               loadTemperatureWithoutCoroutine(objects.toString()){
                   loadWithoutCoroutine(2, it)
               }
            }
            2 -> {
                val temp = objects as Int
                binding.tvTemperature.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonDownload.isEnabled = true
                Log.i("MainActivity", "Load finished $this")
            }
        }
    }

    private fun loadCityWithoutCoroutine(callback: (String) -> Unit) {
        thread {
            Thread.sleep(5000)
            runOnUiThread {
                callback("Moscow")
            }
        }
    }

    private fun loadTemperatureWithoutCoroutine(city: String, callback: (Int) -> Unit) {
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

    private suspend fun loadTemperature(city: String): Int {
        Toast.makeText(
            this,
            "Loading temperature for city: $city",
            Toast.LENGTH_SHORT
        ).show()
        delay(5000)
        return 13
    }

    private suspend fun loadCity(): String {
        delay(5000)
        return "Moscow"
    }
}