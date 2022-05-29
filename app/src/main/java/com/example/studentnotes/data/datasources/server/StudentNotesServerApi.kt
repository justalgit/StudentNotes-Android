package com.example.studentnotes.data.datasources.server

import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.datasources.server.json.LoginRequest
import com.example.studentnotes.data.datasources.server.json.LoginResponse
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.GroupsList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL =  "http://10.0.2.2:8000/api/v1/"

private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val clientBuilder = OkHttpClient.Builder().addInterceptor(loggingInterceptor)

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(clientBuilder.build())
    .addConverterFactory(MoshiConverterFactory.create(moshi).withNullSerialization())
    .baseUrl(BASE_URL)
    .build()


interface StudentNotesServerService {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("initial-data/{user_id}")
    suspend fun getInitialData(
        @Path("user_id") userId: String
    ): InitialDataResponse

    @GET("groups/user/{user_id}")
    suspend fun getUserGroups(
        @Path("user_id") userId: String
    ): GroupsList

    @POST("groups")
    suspend fun createGroup(
        @Body group: Group
    )

    @PUT("groups/{group_id}")
    suspend fun updateGroup(
        @Body group: Group,
        @Path("group_id") groupId: String
    )

    @DELETE("groups/{group_id}")
    suspend fun deleteGroup(
        @Path("group_id") groupId: String
    )

}


object StudentNotesApi {
    val retrofitService : StudentNotesServerService by lazy {
        retrofit.create(StudentNotesServerService::class.java)
    }
}