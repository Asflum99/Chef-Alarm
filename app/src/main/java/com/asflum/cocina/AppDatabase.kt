package com.asflum.cocina

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedConfig::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedConfigDao(): SavedConfigDao
}
