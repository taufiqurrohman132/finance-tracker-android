package com.example.financetrackerapplication.domain.repository

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity

interface AsetRapository {
    suspend fun insertAset(aset: AsetEntity)

    suspend fun updateAset(aset: AsetEntity)

    suspend fun deleteAset(vararg aset: AsetEntity)
}