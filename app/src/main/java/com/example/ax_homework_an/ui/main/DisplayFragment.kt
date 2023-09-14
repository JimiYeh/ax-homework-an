package com.example.ax_homework_an.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ax_homework_an.R
import com.example.ax_homework_an.databinding.FragmentDisplayBinding
import com.example.ax_homework_an.ui.main.view.SingleColumnView
import com.example.ax_homework_an.ui.main.view.SingleItemView
import com.example.ax_homework_an.ui.model.Lottery
import com.example.ax_homework_an.ui.viewBinding
import com.example.ax_homework_an.utils.dpToPx
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Random

class DisplayFragment : Fragment(R.layout.fragment_display) {

    private val binding: FragmentDisplayBinding by viewBinding(FragmentDisplayBinding::bind)
    private val viewModel: MainViewModel by activityViewModel()
    private val colorList: MutableList<Int> = mutableListOf()
    // 記錄目前選中的 row 及 column
    private var currentLottery = Lottery(Lottery.UNDEFINED, Lottery.UNDEFINED)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLottery()

        viewLifecycleOwner.lifecycleScope.launch {
            // 只在 resume 生命週期 才更新畫面
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.lotterySharedFlow
                    .collect { lottery ->
                        Toast.makeText(
                            this@DisplayFragment.requireContext(),
                            "x: ${lottery.x}, y: ${lottery.y}",
                            Toast.LENGTH_SHORT
                        ).show()
                        setNewLottery(lottery)
                    }
            }
        }

        viewModel.enableLotteryTimer(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.enableLotteryTimer(false)
    }

    /**
     *  根據上一頁設定的 row column 生成畫面
     */
    private fun initLottery() {
        initColorList()

        for (column in 0 until viewModel.columnCount) {
            // 先把 column 加進來
            val columnView = SingleColumnView(this.requireContext())
                .apply {
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        setMargins(getMarginPxInt(1), 0, getMarginPxInt(1), 0)
                        weight = 1f
                        // 設定點擊確定時 清除此 column 選中狀態
                        setClickListener(object: SingleColumnView.OnClearButtonClickListener {
                            override fun onClearButtonClick() {
                                clearLottery(column)
                            }
                        })
                    }
                }
            binding.container.addView(columnView)

            // 把每一個 row 分別加進目前 column
            for (row in 0 until viewModel.rowCount) {
                val itemView = SingleItemView(this.requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                    ).apply {
                        setMargins(0, getMarginPxInt(2), 0, getMarginPxInt(2))
                        weight = 1f
                        setColor(colorList[row])
                    }
                }
                columnView.findViewById<LinearLayout>(R.id.lotteryContainer).addView(itemView, 0)
            }
        }
    }

    /**
     * 隨機產生色碼
     */
    private fun initColorList() {
        val random = Random()
        for (i in 0 until viewModel.rowCount)
            colorList.add(
                Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            )
    }

    /**
     *  @param column 清除指定的 column 選中狀態
     */
    private fun clearLottery(column: Int = currentLottery.column) {
        if (column in 0 until viewModel.columnCount) {
            (binding.container.getChildAt(column) as? SingleColumnView)?.run {
                setLottery(false)
            }
        }
    }

    private fun setNewLottery(lottery: Lottery) {
        clearLottery()
        currentLottery = lottery
        if (lottery.column in 0 until viewModel.columnCount)
            (binding.container.getChildAt(lottery.column) as? SingleColumnView)
                ?.setLottery(true, lottery.row)
    }

    private fun getMarginPxInt(dp: Int): Int {
        return dpToPx(this@DisplayFragment.requireContext(), dp).toInt()
    }
}