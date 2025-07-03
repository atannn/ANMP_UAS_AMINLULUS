package com.example.pocketflow.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExpensesWithBudget(
    @Embedded val expenses: Expenses,

    @Relation(
        parentColumn = "idBudget",
        entityColumn = "id"
    )
    val budget: Budget
)