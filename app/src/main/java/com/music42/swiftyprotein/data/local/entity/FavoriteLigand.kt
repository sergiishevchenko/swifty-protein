package com.music42.swiftyprotein.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_ligands",
    indices = [Index(value = ["ligandId"], unique = true)]
)
data class FavoriteLigand(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ligandId: String,
    val createdAtMs: Long = System.currentTimeMillis()
)
