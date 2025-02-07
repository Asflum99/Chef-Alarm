package com.asflum.cocina.database

import android.app.Application
import androidx.room.Room

// Ayuda de la IA
class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()
    }
}
