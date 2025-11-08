package com.example.financetrackerapplication.data.repository

import com.example.financetrackerapplication.data.datasource.local.dao.AsetDao
import com.example.financetrackerapplication.data.datasource.local.dao.CategoryDao
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.CategoryRapository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRapositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRapository {
    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.updateCategory(category)
    }

    override suspend fun deleteCategory(vararg category: CategoryEntity) {
        categoryDao.deleteCategory(*category)
    }

    override fun getCategory(): Flow<List<CategoryEntity>> {
        return categoryDao.getCategory()
    }
}