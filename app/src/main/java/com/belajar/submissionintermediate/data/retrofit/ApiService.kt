package com.belajar.submissionintermediate.data.retrofit

import com.belajar.submissionintermediate.data.response.CommonResponse
import com.belajar.submissionintermediate.data.response.LoginResponse
import com.belajar.submissionintermediate.data.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<CommonResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String
    ) : Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<CommonResponse>
}