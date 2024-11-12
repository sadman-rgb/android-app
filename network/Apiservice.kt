package com.example.cloudstorage.network

import com.example.cloudstorage.models.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("owner") owner: RequestBody
    ): FileUploadResponse

    @GET("download")
    suspend fun downloadFile(@Query("owner") owner: String): ByteArray

    companion object {
        private const val BASE_URL = "http://localhost:8000"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
