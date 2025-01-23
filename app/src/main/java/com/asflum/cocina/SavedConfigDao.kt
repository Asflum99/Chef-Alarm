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

    @Query("SELECT estimatedTime FROM saved_configs WHERE id = :id")
    suspend fun getEstimatedTime(id: Int): Int

    @Query("SELECT foodName FROM saved_configs WHERE id = :id")
    suspend fun getFoodName(id: Int): String

    @Query("SELECT COUNT(1) FROM saved_configs")
    suspend fun getRowCount(): Int

    @Query("DELETE FROM saved_configs WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM saved_configs")
    suspend fun getAllFoodInfo(): List<SavedConfig>

}