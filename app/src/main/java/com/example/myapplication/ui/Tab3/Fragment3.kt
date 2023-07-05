package com.example.myapplication.ui.Tab3

import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.Fragment3Binding
import com.example.myapplication.ui.Tab3.Tab3ViewModel
import kotlin.random.Random
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class Fragment3 : Fragment() {

    private lateinit var shapeImageView: ImageView
    private lateinit var tapCountTextView: TextView
    private lateinit var restartButton: Button
    private lateinit var vibrator: Vibrator
    private var clickCount = 0

    private var _binding: Fragment3Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var boingAnimation: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the "boing" animation from the XML file
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

        val dino = Random.nextInt(1, 5) // Generates a random number between 1 and 5 (inclusive)

        // Set the corresponding image based on the random number
        val imageResource = when (dino) {
            1 -> R.drawable.green
            2 -> R.drawable.star
            3 -> R.drawable.yellow
            4 -> R.drawable.red
            else -> R.drawable.star
        }

        val random = Random.nextInt(10, 51)
        if (clickCount >= random) {
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

}