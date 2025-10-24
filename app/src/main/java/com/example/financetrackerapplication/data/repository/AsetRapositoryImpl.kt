package com.example.financetrackerapplication.data.repository

import com.example.financetrackerapplication.data.datasource.local.dao.AsetDao
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.repository.AsetRapository
import javax.inject.Inject

class AsetRapositoryImpl @Inject constructor(
    private val asetDao: AsetDao
) : AsetRapository {
    override suspend fun insertAset(aset: AsetEntity) {
        asetDao.insertAset(aset)
    }

    override suspend fun updateAset(aset: AsetEntity) {
        asetDao.updateAset(aset)
    }

    override suspend fun deleteAset(vararg aset: AsetEntity) {
        asetDao.deleteAset(*aset)
    }
}