package com.example.image2

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("testing")
    fun preprocessImage(@Part image: MultipartBody.Part): Call<PreprocessedImageResponse>


    companion object {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }.let {
            OkHttpClient.Builder()
                .addInterceptor(it)
                .build()
        }

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .client(logger)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://mobilecomputingproject.onrender.com/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
