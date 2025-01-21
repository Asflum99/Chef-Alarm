package com.asflum.cocina

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_configs")
data class SavedConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val foodQuantity: String,
    val foodMeasurement: String,
    val foodCook: String,
    val foodExtra: String? = null,
    val estimatedTime: Int
)
