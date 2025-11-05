package com.example.financetrackerapplication.domain.repository

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRapository {
    suspend fun insertCategory(category: CategoryEntity)

    suspend fun updateCategory(category: CategoryEntity)

    suspend fun deleteCategory(vararg category: CategoryEntity)

    fun getCategory(): Flow<List<CategoryEntity>>
}