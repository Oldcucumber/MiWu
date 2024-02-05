@file:Suppress("PackageDirectoryMismatch")

package com.github.miwu.miot.device

import android.view.ViewGroup
import com.github.miwu.miot.manager.MiotDeviceManager
import com.github.miwu.miot.quick.MiotBaseQuick
import com.github.miwu.miot.widget.MiotBaseWidget
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices

sealed class DeviceType(
    val device: MiotDevices.Result.Device,
    val layout: ViewGroup,
    val manager: MiotDeviceManager,
) {

    open val type = this::class.java.name.lowercase()// 设备类型

    abstract val isQuickActionable: Boolean // 是否支持快捷操作
    open val isMoreQuick: Boolean = false// 是否支持快捷操作
    open fun getQuickList(): ArrayList<out MiotBaseQuick>? = null

    abstract fun getQuick(): MiotBaseQuick?

    abstract fun onLayout(att: SpecAtt): DeviceType

    inline fun <reified V : MiotBaseWidget<*>> createView(
        siid: Int = -1,
        piid: Int = -1,
        property: SpecAtt.Service.Property? = null,
        action: SpecAtt.Service.Action? = null,
        index: Int = -1
    ) = manager.createView<V>(layout, siid, piid).apply {
        property?.let { properties.add(siid to it) }
        action?.let { actions.add(siid to it) }
        manager.addView(this, index)
    }


}