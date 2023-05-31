package com.example.stabilityGuard.networking

import com.example.stabilityGuard.model.AlarmsResponse
import com.example.stabilityGuard.model.TokenResponse
import com.example.stabilityGuard.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StabilityGuardApiService {
    @GET("alarms")
    suspend fun getAlarms(
        @Query("pageSize") pageSize: Long,
        @Query("page") page: Long,
    ): Response<AlarmsResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body user: User): Response<TokenResponse>

    @POST("alarm/{alarmId}/ack")
    suspend fun acknowledgeAlarm(@Path("alarmId") alarmId: String): Response<Unit>

    @POST("alarm/{alarmId}/clear")
    suspend fun clearAlarm(@Path("alarmId") alarmId: String): Response<Unit>

    @POST("plugins/telemetry/{deviceId}/{scope}")
    @JvmSuppressWildcards
    suspend fun saveDeviceAttributes(
        @Path("deviceId") deviceId: String,
        @Path("scope") scope: String,
        @Body attributes: Map<String, Any>,
    ): Response<Unit>
}
