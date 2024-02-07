package com.github.miwu.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.miwu.MainApplication
import com.github.miwu.databinding.FragmentMainDeviceBinding
import com.github.miwu.databinding.FragmentMainSettingsBinding
import com.github.miwu.logic.preferences.AppPreferences
import com.github.miwu.ui.about.AboutActivity
import com.github.miwu.ui.home.HomeActivity
import com.github.miwu.ui.login.LoginActivity
import com.github.miwu.ui.smart.SmartActivity
import com.github.miwu.viewmodel.MainViewModel
import kndroidx.activity.ViewActivityX
import kndroidx.extension.start
import kndroidx.fragment.ViewFragmentX
import miot.kotlin.Miot

class UserFragment : ViewFragmentX<FragmentMainSettingsBinding, MainViewModel>() {
    override fun init() {
        viewModel.loadInfo()
    }

    fun startHomeActivity() {
        requireContext().start<HomeActivity>()
    }

    fun startAboutActivity() {
        requireContext().start<AboutActivity>()
    }

    fun logout() {
        AppPreferences.apply {
            userId = ""
            securityToken = ""
            homeId = 0
            homeUid = 0
            serviceToken = ""
        }
        requireActivity().start<LoginActivity>()
        requireActivity().finish()
    }

    fun startSmartActivity() {
        requireContext().start<SmartActivity>()
    }
}