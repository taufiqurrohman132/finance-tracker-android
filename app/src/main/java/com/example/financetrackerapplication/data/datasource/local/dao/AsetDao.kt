package com.example.financetrackerapplication.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity

@Dao
interface AsetDao {
    @Insert
    suspend fun insertAset(aset: AsetEntity)

    @Update
    suspend fun updateAset(aset: AsetEntity)

    @Delete
    suspend fun deleteAset(vararg aset: AsetEntity)
}