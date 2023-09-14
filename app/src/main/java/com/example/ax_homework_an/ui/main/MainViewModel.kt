package com.example.ax_homework_an.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ax_homework_an.ui.model.Lottery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.Random

class MainViewModel : ViewModel() {

    companion object {
        private const val DURATION = 10000L // 10秒
    }

    var rowCount: Int = 0
    var columnCount: Int = 0
    private var job: Job? = null

    private val _lotterySharedFlow = MutableSharedFlow<Lottery>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val lotterySharedFlow: SharedFlow<Lottery>
        get() = _lotterySharedFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    // 目前是在第二頁銷毀前都會產生新的 x,y  也可以改成第二頁 onPause 後就停止
    fun enableLotteryTimer(enabled: Boolean) {
        job?.cancel()
        if (enabled) {
            job = viewModelScope.launch(Dispatchers.IO) {
                while (true) {
                    delay(DURATION)
                    _lotterySharedFlow.tryEmit(
                        Lottery(
                            y = Random().nextInt(rowCount) + 1,
                            x = Random().nextInt(columnCount) + 1
                        )
                    )
                }
            }
        } else {
            _lotterySharedFlow.resetReplayCache()
        }
    }
}