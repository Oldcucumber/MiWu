package com.github.miwu.ui.main.fragment

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.miwu.databinding.FragmentMainDeviceBinding
import com.github.miwu.logic.repository.AppRepository
import com.github.miwu.ui.device.DeviceActivity.Companion.startDeviceActivity
import com.github.miwu.viewmodel.MainViewModel
import kndroidx.extension.toast
import kndroidx.fragment.ViewFragmentX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miot.kotlin.model.miot.MiotDevices

class DeviceFragment : ViewFragmentX<FragmentMainDeviceBinding, MainViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    override fun init() {
        binding.swipe.setOnRefreshListener(this)
        lifecycleScope.launch {
            AppRepository.deviceFlow.collectLatest {
                binding.swipe.isRefreshing = false
            }
        }
    }

    fun getRoomName(item: Any?) = AppRepository.getRoomName(item as MiotDevices.Result.Device)

    fun onItemClick(item: Any?) {
        item as MiotDevices.Result.Device
        if (item.isOnline) requireContext().startDeviceActivity(item)
    }

    fun onItemLongClick(item: Any?): Boolean {
        item as MiotDevices.Result.Device
        return true
    }

    override fun onRefresh() {
        binding.swipe.isRefreshing = true
        AppRepository.updateHome()
    }
}