package com.example.ordermyt

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class OrderMyTService {
    private fun initialiseRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("http://10.0.2.2:3000").build()
    }
    fun API(): OrderMyTAPI {
        return initialiseRetrofit().create(OrderMyTAPI::class.java)
    }
}

interface OrderMyTAPI {
    @POST("/api/wss/userLogin")
    suspend fun login(@Body request: Map<String, String>): Response<ResponseBody>

    @POST("/api/wss/orderT")
    suspend fun orderT(@Body request: Map<String, Any>): Response<ResponseBody>
}