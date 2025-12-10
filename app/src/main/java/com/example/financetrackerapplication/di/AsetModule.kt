package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.data.repository.AuthRepositoryImpl
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.usecase.GroupAsetUseCase
import com.example.financetrackerapplication.features.aset.list.AsetViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AsetModule {

    @Provides
    fun provideGroupAsetUseCase(asetRapository: AsetRapository): GroupAsetUseCase =
        GroupAsetUseCase(asetRapository)

//    companion object{
//        // rekomendasi @provide selslau di taruh di object singelton
//    }
}