package com.example.stabilityGuard.networking

import com.example.stabilityGuard.model.AlarmsResponse
import com.example.stabilityGuard.model.TokenResponse
import com.example.stabilityGuard.model.User
import retrofit2.Response
import javax.inject.Inject

class StabilityRepository @Inject constructor(private val apiService: StabilityGuardApiService) {

    suspend fun getAlarms(): Response<AlarmsResponse> {
        return apiService.getAlarms(page = 0, pageSize = 100)
    }

    suspend fun loginUser(user: User): Response<TokenResponse> {
        return apiService.loginUser(user)
    }

    suspend fun ackAlarm(alarmId: String): Response<Unit> {
        return apiService.acknowledgeAlarm(alarmId = alarmId)
    }

    suspend fun clearAlarm(alarmId: String): Response<Unit> {
        return apiService.clearAlarm(alarmId = alarmId)
    }

    suspend fun saveDeviceAttributes(attributes: Map<String, Any>, deviceId: String, scope: String): Response<Unit> {
        return apiService.saveDeviceAttributes(attributes = attributes, deviceId = deviceId, scope = scope)
    }
}
