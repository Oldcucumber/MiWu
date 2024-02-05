package com.github.miwu.miot.device

import android.view.ViewGroup
import com.github.miwu.R
import com.github.miwu.miot.SpecAttHelper
import com.github.miwu.miot.initSpecAttFun
import com.github.miwu.miot.manager.MiotDeviceManager
import com.github.miwu.miot.quick.MiotBaseQuick
import com.github.miwu.miot.widget.AirConditionerBar
import com.github.miwu.miot.widget.DehumidifierBar
import com.github.miwu.miot.widget.SensorText
import com.github.miwu.miot.widget.StatusText
import com.github.miwu.miot.widget.TemperatureControl
import kndroidx.extension.log
import kndroidx.extension.string
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices

class Dehumidifier(
    device: MiotDevices.Result.Device,
    layout: ViewGroup,
    manager: MiotDeviceManager
) : DeviceType(device, layout, manager),
    SpecAttHelper {
    private val bar by lazy { createView<DehumidifierBar>() }

    override val isQuickActionable = false

    override fun getQuick() = null

    override fun onLayout(att: SpecAtt) = forEachAtt(att)

    override fun onPropertyFound(
        siid: Int,
        service: String,
        piid: Int,
        property: String,
        obj: SpecAtt.Service.Property
    ) {
        when (service to property) {
            "dehumidifier" to "on" -> {
                bar.properties.add(siid to obj)
            }

            "dehumidifier" to "mode" -> {
                bar.properties.add(siid to obj)
            }

            "dehumidifier" to "fault" -> {
                createView<StatusText>(siid, piid, obj, index = 0)
            }

            "environment" to "temperature" -> {
                createView<SensorText>(siid, piid, obj, index = 1)
            }

            "environment" to "relative-humidity" -> {
                createView<SensorText>(siid, piid, obj, index = 1)
            }

        }
    }

    override fun onActionFound(
        siid: Int,
        service: String,
        aiid: Int,
        action: String,
        obj: SpecAtt.Service.Action
    ) {

    }
}