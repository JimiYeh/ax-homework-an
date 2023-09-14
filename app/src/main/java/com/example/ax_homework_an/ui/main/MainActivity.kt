package com.example.ax_homework_an.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ax_homework_an.databinding.ActivityMainBinding
import com.example.ax_homework_an.ui.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}