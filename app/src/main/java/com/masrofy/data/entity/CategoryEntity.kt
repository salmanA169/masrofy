package com.masrofy.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey()
    val nameCategory:String,
    val type:String,
    val isPrimary : Boolean
)
