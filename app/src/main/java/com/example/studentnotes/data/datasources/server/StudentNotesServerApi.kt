package com.example.studentnotes.data.datasources.server

import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.datasources.server.json.LoginRequest
import com.example.studentnotes.data.datasources.server.json.LoginResponse
import com.example.studentnotes.data.datasources.server.json.UserToCreate
import com.example.studentnotes.data.entities.*
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

    @POST("users")
    suspend fun createUser(
        @Body userToCreate: UserToCreate
    )

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("initial-data/{user_id}")
    suspend fun getInitialData(
        @Path("user_id") userId: String
    ): InitialDataResponse

    @GET("groups")
    suspend fun getAllGroups(): GroupsList

    @GET("users")
    suspend fun getAllUsers(): UsersList

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

    @POST("events")
    suspend fun createEvent(
        @Body event: Event
    )

    @PUT("events/{event_id}")
    suspend fun updateEvent(
        @Body event: Event,
        @Path("event_id") eventId: String
    )

    @DELETE("events/{event_id}")
    suspend fun deleteEvent(
        @Path("event_id") groupId: String
    )

    @POST("requests")
    suspend fun createRequest(
        @Body request: Request
    )

    @DELETE("requests/{request_id}")
    suspend fun deleteRequest(
        @Path("request_id") groupId: String,
        @Query("is_accept") isAccept: Boolean
    )

    @DELETE("user-group-relations")
    suspend fun leaveGroup(
        @Query("group_id") groupId: String,
        @Query("user_id") userId: String
    )

    @POST("check-event")
    suspend fun checkEvent(
        @Query("event_id") eventId: String,
        @Query("user_id") userId: String
    )

    @POST("change-event-priority")
    suspend fun changeEventPriority(
        @Query("event_id") eventId: String,
        @Query("user_id") userId: String,
        @Query("priority") priority: Int
    )
}


object StudentNotesApi {
    val retrofitService : StudentNotesServerService by lazy {
        retrofit.create(StudentNotesServerService::class.java)
    }
}