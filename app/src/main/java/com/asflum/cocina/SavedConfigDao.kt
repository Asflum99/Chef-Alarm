package com.asflum.cocina

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedConfigDao {
    @Insert
    suspend fun insert(savedConfig: SavedConfig)

    @Query("SELECT * FROM saved_configs")
    suspend fun getAllConfigs(): List<SavedConfig>

    @Query("DELETE FROM saved_configs")
    suspend fun deleteAll()
}