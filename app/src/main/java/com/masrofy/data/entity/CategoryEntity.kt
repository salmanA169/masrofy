package com.masrofy.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category-id",defaultValue = "0")
    val idCategory:Int = 0,
    val nameCategory:String,
    val type:String,
    val isPrimary : Boolean,
    @ColumnInfo(name = "position-category", defaultValue = "0")
    val position : Int
)
