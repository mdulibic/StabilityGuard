package com.example.stabilityGuard.viewModel

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stabilityGuard.model.Alarm
import com.example.stabilityGuard.model.AlarmResponse
import com.example.stabilityGuard.model.AlarmStatus
import com.example.stabilityGuard.model.User
import com.example.stabilityGuard.networking.StabilityRepository
import com.example.stabilityGuard.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val repository: StabilityRepository,
) : ViewModel() {

    private val _alarmsSuccess = MutableLiveData<List<Alarm>>()
    val alarmsSuccess: LiveData<List<Alarm>>
        get() = _alarmsSuccess

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit>
        get() = _loginSuccess

    private val _emailIntent = MutableLiveData<Intent>()
    val emailIntent: LiveData<Intent>
        get() = _emailIntent

    private var isSurveillanceEnabled = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAlarms() {
        viewModelScope.launch {
            val result = repository.getAlarms()
            result.body()?.let {
                val alarms = it.data.map {
                    it.toAlarm()
                }
                    .sortedByDescending { SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(it.timestamp) }
                _alarmsSuccess.value = alarms
                val activeAlarm = alarms.find { it.status == AlarmStatus.ACTIVE_UNACK }
                activeAlarm?.let {
                    if (isSurveillanceEnabled) sendAlertEmail(alarm = activeAlarm)
                }
            }
        }
    }

    fun sendAlertEmail(alarm: Alarm) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("marta.dulibic@fer.hr"))
            putExtra(Intent.EXTRA_SUBJECT, alarm.name)
            putExtra(
                Intent.EXTRA_TEXT,
                "The person fell on ${alarm.timestamp}. Send an ambulance!",
            )
        }
        _emailIntent.value = intent
    }

    fun updateAttributes(attributes: Map<String, Any>) {
        viewModelScope.launch {
            repository.saveDeviceAttributes(
                attributes = attributes,
                deviceId = "b51f6400-f4d8-11ed-993d-8d74c2abdddd",
                scope = "SHARED_SCOPE",
            )
        }
    }

    fun setSurveillance(isEnabled: Boolean) {
        isSurveillanceEnabled = isEnabled
        updateAttributes(attributes = mapOf("surveillanceState" to isEnabled))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun AlarmResponse.toAlarm(): Alarm {
        return Alarm(
            id = this.id.id,
            name = this.name,
            timestamp = this.createdTime.toTimestamp(),
            status = this.status.toAlarmStatus(),
        )
    }

    private fun String.toAlarmStatus(): AlarmStatus {
        return try {
            enumValueOf(this)
        } catch (e: IllegalArgumentException) {
            AlarmStatus.NONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Long.toTimestamp(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val instant = Instant.ofEpochMilli(this)

        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun checkUserStatus() {
        sessionManager.token?.let {
            _loginSuccess.value = Unit
        }
    }

    fun ackAlarm(alarmId: String) {
        viewModelScope.launch {
            repository.ackAlarm(alarmId = alarmId)
        }
    }

    fun clearAlarm(alarmId: String) {
        viewModelScope.launch {
            val result = repository.clearAlarm(alarmId = alarmId)
            if (result.isSuccessful) updateAttributes(attributes = mapOf("ledState" to 1))
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            val result = repository.loginUser(
                user = User(
                    username = username,
                    password = password,
                ),

            )
            result.body()?.let {
                sessionManager.token = it.token
                _loginSuccess.value = Unit
            }
        }
    }
}
