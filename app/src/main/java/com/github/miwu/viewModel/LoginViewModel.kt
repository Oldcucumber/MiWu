package com.github.miwu.viewmodel

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.alexzhirkevich.customqrgenerator.QrCodeGenerator
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrErrorCorrectionLevel
import com.github.alexzhirkevich.customqrgenerator.createQrOptions
import kndroidx.extension.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miot.kotlin.Miot
import miot.kotlin.MiotManager
import miot.kotlin.utils.isNotNull
import miot.kotlin.utils.isNull

class LoginViewModel : ViewModel() {

    val user = ObservableField("")
    val password = ObservableField("")
    val isQrCode = MutableLiveData(true)
    val qrcode = MutableLiveData<Bitmap>()
    val qrcodeMiot = MutableSharedFlow<Miot.MiotUser>()
    val generator = QrCodeGenerator()
    private val job = Job()
    val scope = CoroutineScope(job)

    fun qrcode() {
        job.cancelChildren()
        qrcode.value = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        scope.launch(Dispatchers.IO) {
            MiotManager.getLoginQrCode().apply {
                isNull {
                    withContext(Dispatchers.Main) {
                        "加载二维码失败".toast()
                    }
                }
                isNotNull {
                    val data = QrData.Url(this.data)
                    qrcode.postValue(generator.generateQrCode(data, options))
                    MiotManager.loginByQrCode(loginUrl = loginUrl,
                        onSuccess = {
                            qrcodeMiot.emit(it)
                        },
                        onTimeout = {
                            qrcode()
                        },
                        onFailure = {
                            "登录失败,原因可能为${it?.message ?: "我不到啊"}".toast()
                        }
                    )
                }
            }
        }
    }

    fun change() {
        isQrCode.value = !isQrCode.value!!
    }

    private val options = createQrOptions(512, 512, 0.1f) {
        errorCorrectionLevel = QrErrorCorrectionLevel.Low
    }
}