package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.EcoTerm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [EcoTerm::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ecoTermDao(): EcoTermDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "microeconomia_dict.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.ecoTermDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Ensure if empty on startup, populate it
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val count = database.ecoTermDao().getCount()
                        if (count == 0) {
                            populateDatabase(database.ecoTermDao())
                        }
                    }
                }
            }

            suspend fun populateDatabase(dao: EcoTermDao) {
                dao.insertAll(PreloadedTerms.list)
            }
        }
    }
}
