package io.github.sky130.miwu.ui.framgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.sky130.miwu.MainApplication.Companion.loginMsg
import io.github.sky130.miwu.R
import io.github.sky130.miwu.databinding.FragmentMainSettingsBinding
import io.github.sky130.miwu.logic.dao.UserDAO
import io.github.sky130.miwu.logic.network.miot.UserService
import io.github.sky130.miwu.util.GlideUtils
import java.util.concurrent.Executors

class SettingsFragment : BaseFragment() {
    private lateinit var binding: FragmentMainSettingsBinding
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainSettingsBinding.inflate(layoutInflater)
        val avatar = binding.avatarImage
        val uid = binding.uidText
        val name = binding.nicknameText
        executor.execute {
            val info = UserDAO.getLocalUserInfo()
            val userInfo = if (info.uid.isEmpty()) UserDAO.getLatestUserInfo() else info
            // 更新UI需要切换到主线程
            runOnUiThread {
                if (userInfo.avatar.isNotEmpty()) {
                    GlideUtils.loadImg(userInfo.avatar, avatar)
                } else {
                    avatar.setImageResource(R.drawable.mi_icon_small)
                }
                uid.text = userInfo.uid
                name.text = userInfo.nickname
            }
        }
        return binding.root
    }

}