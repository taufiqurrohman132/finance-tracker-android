package com.example.financetrackerapplication.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsetDao {
    @Insert
    suspend fun insertAset(aset: AsetEntity)

    @Update
    suspend fun updateAset(aset: AsetEntity)

    @Delete
    suspend fun deleteAset(vararg aset: AsetEntity)

    @Query("SELECT * FROM accounts")
    fun getAset(): Flow<List<AsetEntity>>
}