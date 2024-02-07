package com.github.miwu.miot.device

import android.view.ViewGroup
import com.github.miwu.miot.SpecAttClass
import com.github.miwu.miot.manager.MiotDeviceManager
import com.github.miwu.miot.SpecAttHelper
import com.github.miwu.miot.quick.LightQuick
import com.github.miwu.miot.quick.MiotBaseQuick
import com.github.miwu.miot.widget.BrightnessSeekBar
import com.github.miwu.miot.widget.ColorTemperatureSeekbar
import com.github.miwu.miot.widget.Switch
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices

@SpecAttClass("light")
class Light(device: MiotDevices.Result.Device, layout: ViewGroup?, manager: MiotDeviceManager?) :
    DeviceType(device, layout, manager),
    SpecAttHelper {

    private lateinit var quickLight: Pair<Int, Int>
    override val isQuick = true
    override fun getQuick(): MiotBaseQuick {
        return LightQuick(device, quickLight.first, quickLight.second)
    }

    override fun onLayout(att: SpecAtt) = forEachAtt(att)

    override fun onPropertyFound(
        siid: Int,
        service: String,
        piid: Int,
        property: String,
        serviceDesc:String,
        obj: SpecAtt.Service.Property,
    ) {
        when (service to property) {
            "fan" to "fan-level" -> {
                createFanControl(siid, property = obj)
            }

            "fan" to "on" -> {
                createView<Switch>(siid, piid, obj)
            }

            "light" to "on" -> {
                quickLight = siid to piid
                createView<Switch>(siid, piid, obj)
            }

            "light" to "brightness" -> {
                createView<BrightnessSeekBar>(siid, piid).apply {
                    properties.add(siid to obj)
                }
            }

            "light" to "color-temperature" -> {
                createView<ColorTemperatureSeekbar>(siid, piid).apply {
                    properties.add(siid to obj)
                }
            }
        }
    }

    override fun onActionFound(
        siid: Int,
        service: String,
        aiid: Int,
        action: String,
        serviceDesc:String,
        obj: SpecAtt.Service.Action,
    ) {

    }

}