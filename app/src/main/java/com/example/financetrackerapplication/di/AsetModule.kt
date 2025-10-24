package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.data.repository.AuthRepositoryImpl
import com.example.financetrackerapplication.domain.repository.AsetRapository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AsetModule {

    companion object{
        // rekomendasi @provide selslau di taruh di object singelton

    }
}