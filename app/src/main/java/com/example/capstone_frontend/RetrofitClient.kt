package com.example.capstone_frontend

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object {
        var instance: RetrofitClient? = null
            get() {
                if (field == null) {
                    field = RetrofitClient()
                }
                return field
            }
            private set
        lateinit var apiService: ApiService

        // 에뮬레이터 기준 10.0.2.2이며 실제 스마트폰에서 실험시 자신의 ip 주소로 해야함
        private const val baseUrl = "http://10.0.2.2:80"

    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }
}