package com.asflum.cocina

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedConfigDao {
    @Insert
    suspend fun insert(savedConfig: SavedConfig)

    @Query("DELETE FROM saved_configs")
    suspend fun deleteAll()

    @Query("DELETE FROM saved_configs WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM saved_configs")
    suspend fun getAllFoodInfo(): List<SavedConfig>
}