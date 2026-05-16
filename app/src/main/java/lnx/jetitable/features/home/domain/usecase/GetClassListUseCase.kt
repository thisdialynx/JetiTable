package lnx.jetitable.features.home.domain.usecase

import android.icu.util.Calendar
import lnx.jetitable.features.home.domain.models.ScheduleResult
import lnx.jetitable.features.home.domain.repository.ScheduleRepository
import lnx.jetitable.features.home.presentation.ClassUiData
import lnx.jetitable.features.home.presentation.toUi
import lnx.jetitable.misc.DateHelper
import javax.inject.Inject

class GetClassListUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val dateHelper: DateHelper
) {
    suspend operator fun invoke(calendar: Calendar): ScheduleResult<List<ClassUiData>> {
        val formattedTime = dateHelper.timeFormat.format(calendar.time)
        val formattedDate = dateHelper.dateFormat.format(calendar.time)

        return when (val classesNetworkData = scheduleRepository.getClasses(calendar)) {
            is ScheduleResult.Success -> {
                ScheduleResult.Success(
                    classesNetworkData.data.map { it.toUi(formattedDate, formattedTime) }
                )
            }

            is ScheduleResult.Failure -> classesNetworkData
            is ScheduleResult.Loading -> ScheduleResult.Loading
        }
    }
}