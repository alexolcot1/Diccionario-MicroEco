package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EcoTerm
import kotlinx.coroutines.flow.Flow

@Dao
interface EcoTermDao {

    @Query("SELECT * FROM eco_terms ORDER BY term ASC")
    fun getAllTerms(): Flow<List<EcoTerm>>

    @Query("SELECT * FROM eco_terms WHERE isFavorite = 1 ORDER BY term ASC")
    fun getFavoriteTerms(): Flow<List<EcoTerm>>

    @Query("SELECT * FROM eco_terms WHERE id = :id LIMIT 1")
    fun getTermById(id: Int): Flow<EcoTerm?>

    @Query("""
        SELECT * FROM eco_terms 
        WHERE term LIKE '%' || :query || '%' 
           OR summary LIKE '%' || :query || '%' 
           OR meaning LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
        ORDER BY 
          CASE WHEN term LIKE :query || '%' THEN 0 ELSE 1 END,
          term ASC
    """)
    fun searchTerms(query: String): Flow<List<EcoTerm>>

    @Query("SELECT * FROM eco_terms WHERE module = :module ORDER BY term ASC")
    fun getTermsByModule(module: String): Flow<List<EcoTerm>>

    @Query("SELECT * FROM eco_terms WHERE category = :category ORDER BY term ASC")
    fun getTermsByCategory(category: String): Flow<List<EcoTerm>>

    @Query("SELECT COUNT(*) FROM eco_terms")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(term: EcoTerm): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(terms: List<EcoTerm>)

    @Query("UPDATE eco_terms SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("UPDATE eco_terms SET lastViewedTimestamp = :timestamp, viewCount = viewCount + 1 WHERE id = :id")
    suspend fun recordView(id: Int, timestamp: Long)

    @Update
    suspend fun update(term: EcoTerm)

    @Delete
    suspend fun delete(term: EcoTerm)

    @Query("DELETE FROM eco_terms WHERE id = :id")
    suspend fun deleteById(id: Int)
}
