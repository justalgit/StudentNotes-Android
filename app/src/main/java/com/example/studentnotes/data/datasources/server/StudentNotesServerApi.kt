package com.example.studentnotes.data.datasources.server

import com.example.studentnotes.data.datasources.server.json.LoginRequestBody
import com.example.studentnotes.data.datasources.server.json.LoginResponseBody
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL =  "http://10.0.2.2:8000/api/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface StudentNotesServerService {

    @POST("login")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): LoginResponseBody

}


object StudentNotesApi {
    val retrofitService : StudentNotesServerService by lazy {
        retrofit.create(StudentNotesServerService::class.java)
    }
}