package com.example.data.repository

import com.example.data.local.EcoTermDao
import com.example.data.model.EcoTerm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DictionaryRepository(private val dao: EcoTermDao) {

    val allTerms: Flow<List<EcoTerm>> = dao.getAllTerms()

    val favoriteTerms: Flow<List<EcoTerm>> = dao.getFavoriteTerms()

    fun searchTerms(query: String): Flow<List<EcoTerm>> {
        return dao.searchTerms(query.trim())
    }

    fun getTermById(id: Int): Flow<EcoTerm?> {
        return dao.getTermById(id)
    }

    fun getTermsByModule(module: String): Flow<List<EcoTerm>> {
        return dao.getTermsByModule(module)
    }

    suspend fun toggleFavorite(term: EcoTerm) {
        withContext(Dispatchers.IO) {
            dao.updateFavorite(term.id, !term.isFavorite)
        }
    }

    suspend fun recordView(id: Int) {
        withContext(Dispatchers.IO) {
            dao.recordView(id, System.currentTimeMillis())
        }
    }

    suspend fun addTerm(term: EcoTerm): Long {
        return withContext(Dispatchers.IO) {
            dao.insert(term)
        }
    }

    suspend fun updateTerm(term: EcoTerm) {
        withContext(Dispatchers.IO) {
            dao.update(term)
        }
    }

    suspend fun deleteTerm(term: EcoTerm) {
        withContext(Dispatchers.IO) {
            dao.delete(term)
        }
    }
}
