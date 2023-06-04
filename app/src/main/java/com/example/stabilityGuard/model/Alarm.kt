package com.example.stabilityGuard.model

data class Alarm(
    val id: String,
    val deviceId: String,
    val name: String,
    val timestamp: String,
    val status: AlarmStatus,
)

enum class AlarmStatus {
    ACTIVE_ACK, ACTIVE_UNACK, CLEARED_ACK, CLEARED_UNACK, NONE
}
