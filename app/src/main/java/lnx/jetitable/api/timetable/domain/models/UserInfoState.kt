package lnx.jetitable.api.timetable.domain.models

import lnx.jetitable.api.timetable.data.login.AccessResponse

sealed class UserInfoState {
    data class Success(val data: AccessResponse) : UserInfoState()
    data class Failure(val error: InfoRequestFailureReason) : UserInfoState()
}