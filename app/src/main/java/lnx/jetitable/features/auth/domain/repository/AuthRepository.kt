package lnx.jetitable.features.auth.domain.repository

import lnx.jetitable.features.auth.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(login: String, password: String): AuthResult
}