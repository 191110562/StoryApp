package com.mobile.storyapp.api

import com.google.gson.annotations.SerializedName
import com.mobile.storyapp.model.LoginResponse
import com.mobile.storyapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

data class FileUploadResponse(
    @field: SerializedName("error")
    val error: Boolean,

    @field: SerializedName("message")
    val message: String
)

interface ApiService {
    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun fetchStory(): Call<StoryResponse>

    @GET("stories")
    suspend fun getList(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse

    @GET("stories")
    fun getListLocation(
        @Query("location") size: Int,
    ): Call<StoryResponse>
}
