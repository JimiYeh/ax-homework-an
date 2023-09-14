package com.example.ax_homework_an.ui.main.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.ax_homework_an.databinding.ViewSingleItemBinding

class SingleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSingleItemBinding

    init {
        binding = ViewSingleItemBinding.inflate(LayoutInflater.from(context), this)
    }

    var isRandomTextVisible: Boolean
        get() = binding.randomText.isVisible
        set(value) {
            binding.randomText.isVisible = value
        }

    fun setColor(color: Int) {
        binding.footer.setBackgroundColor(color)
        binding.root.setBackgroundColor(
            Color.argb(
                100,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
        )
    }
}