package com.example.ax_homework_an.ui.di

import com.example.ax_homework_an.ui.main.MainViewModel
import org.koin.dsl.module

val appModule = module {
    factory { MainViewModel() }
}