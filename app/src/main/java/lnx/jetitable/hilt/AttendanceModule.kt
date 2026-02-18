package lnx.jetitable.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.repos.AttendanceRepository
import lnx.jetitable.repos.AttendanceRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AttendanceModule {

    @Binds
    @Singleton
    abstract fun bindAttendanceRepo(
        impl: AttendanceRepositoryImpl
    ): AttendanceRepository
}