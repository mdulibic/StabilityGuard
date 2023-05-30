package com.example.stabilityGuard.networking

import com.example.stabilityGuard.model.TelemetryData
import com.example.stabilityGuard.model.TokenResponse
import com.example.stabilityGuard.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StabilityGuardApiService {
    @GET("alarms/{pageSize}/{page}")
    suspend fun getAlarms(@Path("pageSize") pageSize: Long, @Path("page") page: Long): Response<TelemetryData>

    @POST("auth/login")
    suspend fun loginUser(@Body user: User): Response<TokenResponse>
}
