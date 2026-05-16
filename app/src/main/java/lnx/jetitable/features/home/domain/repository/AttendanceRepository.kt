package lnx.jetitable.features.home.domain.repository

import lnx.jetitable.features.home.domain.models.AttendanceResult
import lnx.jetitable.features.home.domain.models.AttendanceVerificationResult
import lnx.jetitable.features.home.presentation.ClassUiData

interface AttendanceRepository {
    suspend fun getAttendanceList(classData: ClassUiData): AttendanceResult
    suspend fun verifyAttendance(classData: ClassUiData): AttendanceVerificationResult
}