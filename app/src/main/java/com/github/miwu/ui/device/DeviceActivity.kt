package com.github.miwu.ui.device

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.github.miwu.MainApplication.Companion.appScope
import com.github.miwu.MainApplication.Companion.gson
import com.github.miwu.databinding.ActivityDeviceBinding
import com.github.miwu.logic.database.model.MiwuDevice.Companion.toMiwu
import com.github.miwu.logic.repository.AppRepository
import com.github.miwu.logic.repository.DeviceRepository
import com.github.miwu.miot.manager.MiotDeviceManager
import com.github.miwu.miot.manager.MiotQuickManager
import com.github.miwu.miot.device.DeviceType
import com.github.miwu.miot.initSpecAttByAnnotation
import com.github.miwu.viewmodel.DeviceViewModel
import kndroidx.activity.ViewActivityX
import kndroidx.extension.start
import kndroidx.extension.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices
import miot.kotlin.utils.parseUrn

class DeviceActivity : ViewActivityX<ActivityDeviceBinding, DeviceViewModel>() {
    private lateinit var device: MiotDevices.Result.Device
    private val layout by lazy { binding.miotWidgetLayout }
    private val manager by lazy { MiotDeviceManager(device, layout) }
    private val urn by lazy { device.specType?.parseUrn() }
    private val mode by lazy { urn?.name }
    private var deviceType: DeviceType? = null

    override fun beforeSetContent() {
        device = gson.fromJson(
            intent.getStringExtra("device"), MiotDevices.Result.Device::class.java
        )
    }

    override fun onResume() {
        super.onResume()
        binding.scroll.requestFocus()
    }


    fun onAddButtonClick() {
        if (deviceType == null) {
            "设备暂不支持".toast()
        } else if (deviceType!!.isMoreQuick) {
            for (i in deviceType!!.getQuickList()!!) {
                MiotQuickManager.addQuick(i)
                "添加成功".toast()
            }
        } else if (deviceType!!.isSwitchQuick) {
            MiotQuickManager.addQuick(deviceType!!.getQuick()!!)
            "添加成功".toast()
        } else {
            "设备没有快捷操作".toast()
        }
    }

    fun onStarButtonClick() {
        appScope.launch {
            DeviceRepository.flow.take(1).collectLatest {
                DeviceRepository.replaceList(ArrayList(it).apply {
                    sortBy { it.index }
                    forEach {
                        if (it.did == device.did) {
                            return@collectLatest
                        }
                    }
                    withContext(Dispatchers.Main) {
                        "设备已添加".toast()
                    }
                    add(device.toMiwu())
                })
            }
        }
    }

    override fun init() {
        viewModel.viewModelScope.launch {
            device.specType?.also {
                val att = AppRepository.getDeviceSpecAtt(it)
                initSpecAtt(att ?: return@launch "设备不支持哦哦哦".toast())
            }.let {
                if (it == null)
                    "设备不支持哦哦哦".toast()
            }
        }
    }

    private fun initSpecAtt(att: SpecAtt) {
        mode ?: return // TODO
        deviceType = initSpecAttByAnnotation(device, mode!!, att, layout, manager)
        manager.post(700L)
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
