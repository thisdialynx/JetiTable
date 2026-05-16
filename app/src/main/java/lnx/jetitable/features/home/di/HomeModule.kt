package lnx.jetitable.features.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.features.home.data.repository.AttendanceRepositoryImpl
import lnx.jetitable.features.home.data.repository.ScheduleRepositoryImpl
import lnx.jetitable.features.home.domain.repository.AttendanceRepository
import lnx.jetitable.features.home.domain.repository.ScheduleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(
        impl: AttendanceRepositoryImpl
    ): AttendanceRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        impl: ScheduleRepositoryImpl
    ): ScheduleRepository
}