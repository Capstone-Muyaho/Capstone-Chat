package com.example.capstone_frontend

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "5bec60bab0b4893040677633105f1a77")
    }
}