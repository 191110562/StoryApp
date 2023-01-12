package com.mobile.storyapp.api

import com.mobile.storyapp.model.UserPreference
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    fun getApiService(userPreference: UserPreference): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor{
                val original = it.request()
                var token: String?
                runBlocking {
                    token = userPreference.getToken()
                    if (!token.isNullOrEmpty()){
                        val authorized = original.newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        it.proceed(authorized)
                    }else{
                        it.proceed(original)
                    }
                }
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}