package lnx.jetitable.features.about.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.features.about.data.repository.AboutRepositoryImpl
import lnx.jetitable.features.about.domain.repository.AboutRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AboutModule {

    @Binds
    @Singleton
    abstract fun bindAboutRepository(
        impl: AboutRepositoryImpl
    ): AboutRepository
}