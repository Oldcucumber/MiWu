@file:Suppress("PackageDirectoryMismatch")

package com.github.miwu.miot.device

import android.view.ViewGroup
import com.github.miwu.miot.MiotDeviceManager
import com.github.miwu.miot.quick.MiotBaseQuick
import com.github.miwu.miot.widget.MiotBaseWidget
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices
import miot.kotlin.utils.Urn

sealed class DeviceType(val device:   MiotDevices.Result.Device, val layout: ViewGroup, val manager: MiotDeviceManager) {

    open val type = this::class.java.name.lowercase()// 设备类型

    abstract val isQuickActionable: Boolean // 是否支持快捷操作

    abstract fun getQuick(): MiotBaseQuick?

    abstract fun onLayout(att: SpecAtt): DeviceType

    inline fun <reified V : MiotBaseWidget<*>> createAddView(
        siid: Int = -1,
        piid: Int = -1,
        property: SpecAtt.Service.Property? = null,
    ) = manager.createAddView<V>(layout, siid, piid, property)


}