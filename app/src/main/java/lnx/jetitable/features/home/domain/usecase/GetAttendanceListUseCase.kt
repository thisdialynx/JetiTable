package lnx.jetitable.features.home.domain.usecase

import lnx.jetitable.features.home.domain.models.AttendanceResult
import lnx.jetitable.features.home.domain.repository.AttendanceRepository
import lnx.jetitable.features.home.presentation.ClassUiData
import javax.inject.Inject

class GetAttendanceListUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
) {
    suspend operator fun invoke(classData: ClassUiData): AttendanceResult {
        return attendanceRepository.getAttendanceList(classData)
    }
}