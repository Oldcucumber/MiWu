package com.github.miwu.logic.preferences

import kndroidx.preference.PreferencesX
import miot.kotlin.MiotManager

object AppPreferences : PreferencesX("app") {

    var userId by stringPreference("userId", "")

    var securityToken by stringPreference("securityToken", "")

    var serviceToken by stringPreference("serviceToken", "")

    val deviceId by lazy { MiotManager.getRandomDeviceId() }

    var homeId by longPreference("homeId", 0L)

    var homeUid by longPreference("homeUid", 0L)


}