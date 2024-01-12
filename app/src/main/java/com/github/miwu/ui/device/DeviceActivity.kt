package com.github.miwu.ui.device

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.github.miwu.MainApplication.Companion.gson
import com.github.miwu.databinding.ActivityDeviceBinding
import com.github.miwu.miot.MiotDeviceManager
import com.github.miwu.miot.device.Light
import com.github.miwu.viewmodel.DeviceViewModel
import kndroidx.activity.ViewActivityX
import kndroidx.extension.start
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miot.kotlin.MiotManager
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices
import miot.kotlin.utils.parseUrn

class DeviceActivity : ViewActivityX<ActivityDeviceBinding, DeviceViewModel>() {
    private lateinit var device: MiotDevices.Result.Device
    private val layout by lazy { binding.miotWidgetLayout }
    private val manager by lazy { MiotDeviceManager(device, layout) }
    private val urn by lazy { device.specType?.parseUrn() }
    private val mode by lazy { urn?.type }

    override fun beforeSetContent() {
        device = gson.fromJson(
            intent.getStringExtra("device"),
            MiotDevices.Result.Device::class.java
        )
    }

    override fun init() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            device.specType?.also {
                val att = MiotManager.getSpecAttWithLanguage(it)
                withContext(Dispatchers.IO) {
                    att?.let { att ->
                        initSpecAtt(att)
                    }
                }
            }.let {

            }
        }
    }

    private fun initSpecAtt(att: SpecAtt) {
        // 未来考虑换成注解
        when (mode) {
            "light" -> {
                Light(layout, manager).onLayout(att)
            }
        }
        manager.post(1000L)
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.destroy()
    }

    companion object {
        fun Context.startDeviceActivity(device: MiotDevices.Result.Device) {
            start<DeviceActivity> {
                putExtra("device", gson.toJson(device))
            }
        }
    }
}