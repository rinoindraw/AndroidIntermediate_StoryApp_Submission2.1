package com.rinoindraw.storybismillah.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rinoindraw.storybismillah.database.repository.AuthRepository
import com.rinoindraw.storybismillah.database.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): Flow<Result<LoginResponse>> = authRepository.loginUser(email, password)

}