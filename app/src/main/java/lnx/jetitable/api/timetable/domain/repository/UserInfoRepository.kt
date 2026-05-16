package lnx.jetitable.api.timetable.domain.repository

import lnx.jetitable.api.timetable.domain.models.UserInfoState

interface UserInfoRepository {
    suspend fun refreshUserInfo(): UserInfoState
}