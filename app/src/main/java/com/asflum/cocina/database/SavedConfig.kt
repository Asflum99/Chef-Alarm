package com.asflum.cocina.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Ayuda de la IA para poder implementar las columnas de la base de datos
@Entity(tableName = "saved_configs")
data class SavedConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val foodQuantity: String,
    val foodMeasurement: String,
    val foodCook: String,
    val foodExtra: String? = null,
    val estimatedTime: Int,
    val potatoType: String? = null,
    val potatoCut: String? = null
)
