package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.data.repository.AuthRepositoryImpl
import com.example.financetrackerapplication.domain.repository.AuthRepository
import com.example.financetrackerapplication.domain.usecase.GetUserStatusUseCase
import com.example.financetrackerapplication.domain.usecase.SignInAnonymouslyUseCase
import com.example.financetrackerapplication.domain.usecase.SignInWithGoogleUseCase
import com.example.financetrackerapplication.domain.usecase.SignInWithLinkEmailUseCase
import com.example.financetrackerapplication.domain.usecase.SignOutUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideSignInAnonymouslyUseCase(repo: AuthRepository) =
        SignInAnonymouslyUseCase(repo)

    @Provides
    fun provideGetUserStatusUseCase(repo: AuthRepository) =
        GetUserStatusUseCase(repo)

    @Provides
    fun provideSignInWithLinkEmailUseCase(repo: AuthRepository) =
        SignInWithLinkEmailUseCase(repo)

    @Provides
    fun provideSignInWithGoogleUseCase(repo: AuthRepository) =
        SignInWithGoogleUseCase(repo)

    @Provides
    fun provideSignOutUseCase(repo: AuthRepository) =
        SignOutUseCase(repo)

}
