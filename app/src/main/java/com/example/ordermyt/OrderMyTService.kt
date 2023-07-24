package com.example.ordermyt

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object OrderMyTService {
    private fun initialiseRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("http://10.0.2.2:3000").build()
    }
   fun API(): OrderMyTAPI {
        return initialiseRetrofit().create(OrderMyTAPI::class.java)
    }

   fun userLogin(mobile: JSONObject): JSONObject{

           var url = URL("http://10.0.2.2:3000/api/wss/userLogin",)
           with(url.openConnection() as HttpURLConnection) {
               readTimeout = 100
               connectTimeout = 100
               requestMethod = "POST"
               doOutput = true
               setRequestProperty("Content-Type", "application/json")
               val outputStream = DataOutputStream(getOutputStream())
               outputStream.write(mobile.toString().toByteArray(Charsets.UTF_8))
               outputStream.flush()
               outputStream.close()

               return JSONObject( inputStream.bufferedReader().use {it.readText()})

           }




    }

    fun getOrders(id: String): JSONObject {
        println("http://10.0.2.2:3000/api/wss/orders?${URLEncoder.encode(id,"UTF-8")}")
        val url = URL("http://10.0.2.2:3000/api/wss/orders?id=${URLEncoder.encode(id,"UTF-8")}").readText()
        return JSONObject(url)
    }

    fun getPoints(id: String): JSONObject {
        println("http://10.0.2.2:3000/api/wss/points?${URLEncoder.encode(id,"UTF-8")}")
        val url = URL("http://10.0.2.2:3000/api/wss/points?id=${URLEncoder.encode(id,"UTF-8")}").readText()
        return JSONObject(url)
    }

    fun getPopular(id: String): JSONObject {
        println("http://10.0.2.2:3000/api/wss/popular?${URLEncoder.encode(id,"UTF-8")}")
        val url = URL("http://10.0.2.2:3000/api/wss/popular?id=${URLEncoder.encode(id,"UTF-8")}").readText()
        return JSONObject(url)
    }
    fun orderT(order: JSONObject): JSONObject{
        var url = URL("http://10.0.2.2:3000/api/wss/orderT",)
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            val outputStream = DataOutputStream(getOutputStream())
            outputStream.write(order.toString().toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()
            return JSONObject( inputStream.bufferedReader().use {it.readText()})

        }

    }
}

interface OrderMyTAPI {
    @POST("/api/wss/userLogin")
    suspend fun login(@Body request: Map<String, String>): Response<ResponseBody>

    @POST("/api/wss/orderT")
    suspend fun orderT(@Body request: Map<String, Any>): Response<ResponseBody>
}