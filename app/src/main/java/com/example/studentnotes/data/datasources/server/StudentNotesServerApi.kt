package com.example.studentnotes.data.datasources.server

import androidx.lifecycle.LiveData
import com.example.studentnotes.data.datasources.server.json.LoginRequestBody
import com.example.studentnotes.data.datasources.server.json.LoginResponseBody
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.GroupsList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

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

    @GET("groups/user/{user_id}")
    suspend fun getUserGroups(
        @Path("user_id") userId: String
    ): GroupsList

}


object StudentNotesApi {
    val retrofitService : StudentNotesServerService by lazy {
        retrofit.create(StudentNotesServerService::class.java)
    }
}