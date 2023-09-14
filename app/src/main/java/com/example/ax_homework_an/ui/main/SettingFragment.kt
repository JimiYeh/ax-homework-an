package com.example.ax_homework_an.ui.main

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.ax_homework_an.R
import com.example.ax_homework_an.databinding.FragmnetSettingBinding
import com.example.ax_homework_an.ui.viewBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.regex.Pattern

class SettingFragment : Fragment(R.layout.fragmnet_setting) {

    private val binding: FragmnetSettingBinding by viewBinding(FragmnetSettingBinding::bind)
    private val viewModel: MainViewModel by activityViewModel()
    private val rangeFilter by lazy {
        object : InputFilter {
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence {
                if (dstart == 0) {
                    if (source.startsWith('0')) {
                        val firstNonZeroIndex = source.indexOfFirst { char -> char != '0' }
                        return if (firstNonZeroIndex == -1) "" else source.substring(firstNonZeroIndex)
                    }
                }
                return source
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rowEditText.filters = arrayOf(*rowEditText.filters, rangeFilter)
            rowEditText.doAfterTextChanged {
                checkInput()
            }

            columnEditText.filters = arrayOf(*columnEditText.filters, rangeFilter)
            columnEditText.doAfterTextChanged {
                checkInput()
            }

            confirmButton.setOnClickListener {

            }
        }
    }

    private fun checkInput() {
        fun String.inRange(): Boolean {
            return Pattern.compile("(^1\\d\$)|(^\\d\$)|20").matcher(this).find()
        }

        var rowCountInRange = false
        var columnCountInRange = false

        binding.rowEditText.text?.toString()?.let { rowCount ->
            rowCountInRange = rowCount.inRange()
            binding.rowField.error = if (rowCount.isBlank() || rowCountInRange) null else getString(R.string.range_error)
        } ?: run {
            binding.rowField.error = null
        }

        binding.columnEditText.text?.toString()?.let { columnCount ->
            columnCountInRange = columnCount.inRange()
            binding.columnField.error =
                if (columnCount.isBlank() || columnCountInRange) null else getString(R.string.range_error)
        } ?: run {
            binding.columnField.error = null
        }

        binding.confirmButton.isEnabled = rowCountInRange && columnCountInRange
    }
}