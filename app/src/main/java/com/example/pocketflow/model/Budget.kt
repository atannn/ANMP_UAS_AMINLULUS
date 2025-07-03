package com.example.pocketflow.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val nominal: Int,
    val budgetLeft: Int,
    val idUser: Int
)