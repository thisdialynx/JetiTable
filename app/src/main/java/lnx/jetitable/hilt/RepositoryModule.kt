package lnx.jetitable.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.repos.ScheduleRepository
import lnx.jetitable.repos.ScheduleRepositoryImpl
import lnx.jetitable.repos.UserInfoRepository
import lnx.jetitable.repos.UserInfoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScheduleRepo(
        impl: ScheduleRepositoryImpl
    ): ScheduleRepository

    @Binds
    @Singleton
    abstract fun bindUserInfoRepo(
        impl: UserInfoRepositoryImpl
    ): UserInfoRepository
}