package com.example.pocketflow.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expenses(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val idBudget: Int,
    val nominal: Int,
    val desc: String,
    val idUser: Int
)