package com.example.stabilityGuard.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stabilityGuard.model.TelemetryData
import com.example.stabilityGuard.model.User
import com.example.stabilityGuard.networking.StabilityRepository
import com.example.stabilityGuard.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val repository: StabilityRepository,
) : ViewModel() {

    private val _alarmsSuccess = MutableLiveData<TelemetryData>()
    val alarmsSuccess: LiveData<TelemetryData>
        get() = _alarmsSuccess

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit>
        get() = _loginSuccess

    fun getTelemetryData() {
        viewModelScope.launch {
            repository.getAlarms()
        }
    }

    fun checkUserStatus() {
        sessionManager.token?.let {
            _loginSuccess.value = Unit
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
