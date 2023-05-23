package com.example.stabilityGuard.networking

import com.example.stabilityGuard.model.TelemetryData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StabilityGuardApiService {
    @GET("telemetry/{deviceId}")
    fun getTelemetry(@Path("deviceId") deviceId: String): Response<List<TelemetryData>>
}
