package com.github.miwu.miot.device

import android.view.ViewGroup
import com.github.miwu.miot.SpecAttClass
import com.github.miwu.miot.SpecAttHelper
import com.github.miwu.miot.manager.MiotDeviceManager
import com.github.miwu.miot.widget.Button
import com.github.miwu.miot.widget.FeederPlanList
import com.github.miwu.miot.widget.StatusText
import miot.kotlin.model.att.SpecAtt
import miot.kotlin.model.miot.MiotDevices

@SpecAttClass("pet-feeder")
class Feeder(
    device: MiotDevices.Result.Device, layout: ViewGroup?, manager: MiotDeviceManager?
) : DeviceType(device, layout, manager), SpecAttHelper {
    private val list by lazy { createView<FeederPlanList>() }

    override val isSwitchQuick = false

    override fun getQuick() = null

    override fun onLayout(att: SpecAtt) = forEachAtt(att)

    override fun onPropertyFound(
        siid: Int,
        service: String,
        piid: Int,
        property: String,
        serviceDesc: String,
        obj: SpecAtt.Service.Property,
    ) {
        when (service to property) {
            "pet-feeder" to "pet-food-left-level" -> {
                createView<StatusText>(siid, piid, obj)
            }
        }
    }

    override fun onActionFound(
        siid: Int,
        service: String,
        aiid: Int,
        action: String,
        serviceDesc: String,
        obj: SpecAtt.Service.Action,
    ) {
        when (service to action) {
            "pet-feeder" to "pet-food-out" -> {
                createView<Button>(action = obj)
            }

            "feedplanserve" to "getfeedplan" -> {
                list.actions.add(siid to obj)
                list.siid = siid
            }
        }
    }
}