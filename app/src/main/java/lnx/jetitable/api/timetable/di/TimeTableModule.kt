package lnx.jetitable.api.timetable.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.api.timetable.domain.repository.UserInfoRepository
import lnx.jetitable.api.timetable.data.repository.UserInfoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeTableModule {

    @Binds
    @Singleton
    abstract fun bindUserInfoRepo(
        impl: UserInfoRepositoryImpl
    ): UserInfoRepository
}