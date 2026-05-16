package lnx.jetitable.features.settings.domain.usecase

import lnx.jetitable.api.timetable.domain.models.EducationForm
import lnx.jetitable.api.timetable.domain.models.User
import lnx.jetitable.features.settings.domain.model.ProfileError
import lnx.jetitable.features.settings.domain.model.UserInfoData
import lnx.jetitable.features.settings.domain.model.UserProfileState
import lnx.jetitable.misc.DateHelper
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val dateHelper: DateHelper
) {
    operator fun invoke(user: User): UserProfileState {
        val educationForm = when (user.isFullTime) {
            0 -> EducationForm.PART_TIME
            1 -> EducationForm.FULL_TIME
            else -> EducationForm.UNKNOWN
        }
        val semester = dateHelper.getSemester()
        val academicYears = dateHelper.getAcademicYears()

        val data = UserInfoData(
            fullName = user.fullName,
            group = user.group,
            academicYears = academicYears,
            status = user.status,
            educationForm = educationForm,
            semesterType = semester
        )

        return if (user.fullName.isEmpty()) {
            UserProfileState.Failure(ProfileError.NO_DATA)
        } else {
            UserProfileState.Success(data)
        }
    }
}