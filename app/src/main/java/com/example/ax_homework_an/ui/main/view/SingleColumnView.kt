package com.example.ax_homework_an.ui.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.ax_homework_an.databinding.ViewSingleColumnBinding

class SingleColumnView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    interface OnClearButtonClickListener {
        fun onClearButtonClick()
    }

    private val binding: ViewSingleColumnBinding
    // 是否顯示外框
    private var isHighlight = false
    // 需要顯示 random 的格子  null 表示沒有
    private var highlightChildIndex: Int? = null
    private var clickListener: OnClearButtonClickListener? = null

    init {
        binding = ViewSingleColumnBinding.inflate(LayoutInflater.from(context), this)
        binding.clearButton.setOnClickListener {
            clickListener?.onClearButtonClick()
        }
    }

    /**
     *  @param isHighlight 是否被選中 (顯示外框)
     *  @param childIndex 要顯示 random 的格子 null 表示沒有
     */
    fun setLottery(isHighlight: Boolean, childIndex: Int? = null) {
        this.isHighlight = isHighlight
        binding.clearButton.isEnabled = isHighlight
        binding.border.isVisible = isHighlight

        when {
            !this.isHighlight -> { // 取消 highlight 同時消除 random 顯示
                highlightChildIndex?.let {
                    setLotteryItem(it, false)
                }
            }
            else -> {
                if (highlightChildIndex != childIndex) {
                    highlightChildIndex?.let {
                        setLotteryItem(it, false)
                    }
                    childIndex?.let {
                        setLotteryItem(it, true)
                    }
                }
            }
        }
        highlightChildIndex = childIndex
    }

    fun setClickListener(listener: OnClearButtonClickListener) {
        clickListener = listener
    }

    private fun setLotteryItem(index: Int, showRandomWord: Boolean) {
        (binding.lotteryContainer.getChildAt(index) as? SingleItemView)?.isRandomTextVisible = showRandomWord
    }


}