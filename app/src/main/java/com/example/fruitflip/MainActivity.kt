package com.example.fruitflip

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.fruitflip.databinding.ActivityMainBinding
import java.util.Random

//which allows you to bind your application's UI components to data sources in your app
//handler-primarily used to schedule messages and runnables to be executed at some point in the future

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var score = 0
    private var highScore = 0
    private val imageArray = ArrayList<ImageView>()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.catchFruits = this

        val sharedPref = this.getPreferences(MODE_PRIVATE)
        highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), 0)
        binding.highScore.text = "High Score : $highScore"

        score = 0
        binding.score = getString(R.string.score_0)
        imageArray.addAll(
            listOf(
                binding.ivApple, binding.ivBanana, binding.ivCherry,
                binding.ivGrapes, binding.ivKiwi, binding.ivOrange,
                binding.ivPear, binding.ivStrawberry, binding.ivWatermelon
            )
        )
        hideImages()
        playAndRestart()
    }

    private fun hideImages() {
        runnable = Runnable {
            imageArray.forEach { it.visibility = View.INVISIBLE }
            imageArray[Random().nextInt(imageArray.size)].visibility = View.VISIBLE
            handler.postDelayed(runnable, 500)
        }
        handler.post(runnable)
    }

    @SuppressLint("SetTextI18n")
    fun increaseScore() {
        score++
        binding.score = "Score : $score"
        if (score > highScore) {
            highScore = score
            binding.highScore.text = "High Score : $highScore"

            val sharedPref = this.getPreferences(MODE_PRIVATE)
            with (sharedPref.edit()) {
                putInt(getString(R.string.saved_high_score_key), highScore)
                apply()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun playAndRestart() {
        score = 0
        binding.score = "Score : 0"

        hideImages()
        binding.time = "Time : 10"
        imageArray.forEach { it.visibility = View.INVISIBLE }

        object : CountDownTimer(10000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.time = getString(R.string.time_up)
                handler.removeCallbacks(runnable)

                AlertDialog.Builder(this@MainActivity).apply {
                    setCancelable(false)
                    setTitle(getString(R.string.game_name))
                    setMessage("Your score : $score\nWould you like to play again?")
                    setPositiveButton(getString(R.string.yes)) { _, _ -> playAndRestart() }
                    setNegativeButton(getString(R.string.no)) { _, _ ->
                        score = 0
                        binding.score = "Score : 0"
                        binding.time = "Time : 0"
                        imageArray.forEach { it.visibility = View.INVISIBLE }
                        finish()
                    }
                }.create().show()
            }
            @SuppressLint("SetTextI18n")
            override fun onTick(tick: Long) {
                binding.time = "Time : " + tick / 1000
            }
        }.start()
    }
}