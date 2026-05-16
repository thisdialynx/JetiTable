package lnx.jetitable.features.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.features.settings.domain.repository.AppUpdateRepository
import lnx.jetitable.features.settings.domain.repository.SettingsRepository
import lnx.jetitable.features.settings.repository.AppUpdateRepositoryImpl
import lnx.jetitable.features.settings.repository.SettingsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindAppUpdateRepository(
        impl: AppUpdateRepositoryImpl
    ): AppUpdateRepository

    @Binds
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}