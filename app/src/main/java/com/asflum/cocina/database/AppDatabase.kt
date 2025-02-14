package com.asflum.cocina.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Ayuda de la IA para implementar esta base de datos y sus cambios de versiones
@Database(entities = [SavedConfig::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedConfigDao(): SavedConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build().also { INSTANCE = it }
            }
        }
    }
}
