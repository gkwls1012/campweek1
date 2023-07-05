package com.example.myapplication.ui.Tab3

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.Fragment3Binding
import kotlin.random.Random

class Fragment3 : Fragment() {

    private lateinit var shapeImageView: ImageView
    private lateinit var tapCountTextView: TextView
    private lateinit var restartButton: Button
    private lateinit var vibrator: Vibrator
    private var clickCount = 0
    private var imageResourceCount = mutableMapOf<Int, Int>()
    private lateinit var boingAnimation: Animation
    private lateinit var sharedPreferences: SharedPreferences

    private var _binding: Fragment3Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.boing_animation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tab3ViewModel = ViewModelProvider(this).get(Tab3ViewModel::class.java)
        _binding = Fragment3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        val textView: TextView = binding.textNotifications
        tab3ViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        shapeImageView = root.findViewById(R.id.shapeImageView)
        tapCountTextView = root.findViewById(R.id.tapCountTextView)
        restartButton = root.findViewById(R.id.restartButton)
        vibrator = requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator

        shapeImageView.setOnClickListener { onShapeClick() }
        restartButton.setOnClickListener { onRestartClick() }

        // Retrieve the saved image resource counts from shared preferences
        val imageResourceCountString = sharedPreferences.getString("imageResourceCount", "")
        if (imageResourceCountString?.isNotEmpty() == true) {
            imageResourceCount = deserializeImageResourceCount(imageResourceCountString)
            updateCounterTextView()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onShapeClick() {
        clickCount++
        tapCountTextView.text = clickCount.toString()

        val random = Random.nextInt(10, 51)
        if (clickCount >= random) {
            val dino = Random.nextInt(1, 5)
            val imageResource = when (dino) {
                1 -> {
                    increaseImageResourceCount(R.drawable.green)
                    R.drawable.green
                }
                2 -> {
                    increaseImageResourceCount(R.drawable.star)
                    R.drawable.star
                }
                3 -> {
                    increaseImageResourceCount(R.drawable.yellow)
                    R.drawable.yellow
                }
                4 -> {
                    increaseImageResourceCount(R.drawable.red)
                    R.drawable.red
                }
                else -> {
                    increaseImageResourceCount(R.drawable.star)
                    R.drawable.star
                }
            }
            shapeImageView.setImageResource(imageResource)
            shapeImageView.startAnimation(boingAnimation)
            restartButton.visibility = View.VISIBLE
            shapeImageView.isEnabled = false
        }

        vibrateDevice()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateDevice() {
        val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibrationEffect)
    }

    private fun onRestartClick() {
        clickCount = 0
        tapCountTextView.text = clickCount.toString()
        shapeImageView.setImageResource(R.drawable.oval)
        restartButton.visibility = View.GONE
        shapeImageView.isEnabled = true
    }

    private fun increaseImageResourceCount(resourceId: Int) {
        val count = imageResourceCount.getOrDefault(resourceId, 0)
        imageResourceCount[resourceId] = count + 1
        updateCounterTextView()

        // Save the image resource counts to shared preferences
        val editor = sharedPreferences.edit()
        editor.putString("imageResourceCount", serializeImageResourceCount(imageResourceCount))
        editor.apply()
    }

    private fun updateCounterTextView() {
        val counterTextView = binding.counterTextView
        val countText = StringBuilder()

        for ((resourceId, count) in imageResourceCount) {
            val name = when (resourceId) {
                R.drawable.yellow -> "Yellow dino"
                R.drawable.green -> "Green dino"
                R.drawable.star -> "Blue dino"
                R.drawable.red -> "Red dino"
                else -> ""
            }
            countText.append(name)
                .append(": ")
                .append(count)
                .append("\n")
        }
        counterTextView.text = countText.toString()
    }

    private fun serializeImageResourceCount(countMap: Map<Int, Int>): String {
        val countString = StringBuilder()
        for ((key, value) in countMap) {
            countString.append(key).append(":").append(value).append(",")
        }
        return countString.toString()
    }

    private fun deserializeImageResourceCount(countString: String): MutableMap<Int, Int> {
        val countMap = mutableMapOf<Int, Int>()
        val pairs = countString.split(",")
        for (pair in pairs) {
            val keyValue = pair.split(":")
            if (keyValue.size == 2) {
                val resourceId = keyValue[0].toIntOrNull()
                val count = keyValue[1].toIntOrNull()
                if (resourceId != null && count != null) {
                    countMap[resourceId] = count
                }
            }
        }
        return countMap
    }
}
