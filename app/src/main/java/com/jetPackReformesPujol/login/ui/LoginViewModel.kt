package com.jetPackReformesPujol.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetPackReformesPujol.login.data.UserPreferences
import com.jetPackReformesPujol.login.domain.model.Response
import com.jetPackReformesPujol.login.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val dataStore: UserPreferences
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _signInResponse = MutableLiveData<Response<Boolean>>(Response.Success(false))
    val signInResponse: LiveData<Response<Boolean>> = _signInResponse

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        viewModelScope.launch {
            val email = dataStore.userEmail.first()
            val password = dataStore.userPassword.first()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInWithEmailAndPassword(email, password)
            }
        }
    }

    fun saveUserCredentials(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveUserCredentials(email, password)
        }
    }

    fun checkAuthState() {
        repo.getAuthState(viewModelScope).onEach { isAuthenticated ->
            _isAuthenticated.value = isAuthenticated
        }.launchIn(viewModelScope)
    }

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnable.value = onLoginConfirm(email, password)
    }

    fun onLoginConfirm(email: String, password: String): Boolean {
        return (email == "jailet84c@gmail.com" && password == "didipodi84")
    }

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        _signInResponse.value = Response.Loading
        val result = repo.firebaseSignInWithEmailAndPassword(email, password)
        when (result) {
            is Response.Success -> {
                _isAuthenticated.value = true
                _signInResponse.value = Response.Success(true)
            }
            is Response.Error -> {
                _signInResponse.value = Response.Error(result.e)
            }
            else -> {}
        }
    }
}
