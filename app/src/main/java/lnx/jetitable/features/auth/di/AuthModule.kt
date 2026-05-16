package lnx.jetitable.features.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.features.auth.data.repository.AuthRepositoryImpl
import lnx.jetitable.features.auth.data.repository.PasswordRecoveryRepositoryImpl
import lnx.jetitable.features.auth.domain.repository.AuthRepository
import lnx.jetitable.features.auth.domain.repository.PasswordRecoveryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPasswordRecoverRepository(
        impl: PasswordRecoveryRepositoryImpl
    ): PasswordRecoveryRepository
}