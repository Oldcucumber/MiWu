package com.github.miwu.miot.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.databinding.adapters.SeekBarBindingAdapter.OnProgressChanged
import com.github.miwu.databinding.MiotWidgetFanThumbSeekbarBinding as Binding

class FanSeekbar(context: Context) : MiotBaseWidget<Binding>(context),
    OnSeekBarChangeListener {


    override fun init() {
        binding.seekbar.setOnSeekBarChangeListener(this)
        properties[0].second.let {
            binding.seekbar.setMinProgress(it.valueRange!![0].toInt())
            binding.seekbar.setMaxProgress(it.valueRange!![1].toInt())
        }
    }

    override fun onValueChange(value: Any) {
        value as Number
        binding.seekbar.setCurrentProgress(value.toInt())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

    override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar) {
        stopRefresh()
    }

    override fun onStopTrackingTouch(p0: SeekBar) {
        startRefresh()
        putValue(binding.seekbar.getCurrentProgress())
    }

}