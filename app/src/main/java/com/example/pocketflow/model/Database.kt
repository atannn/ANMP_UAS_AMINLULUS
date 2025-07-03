package com.example.pocketflow.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pocketflow.model.*

@Database(entities = [User::class, Budget::class, Expenses::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): DaoUser
    abstract fun budgetDao(): DaoBudget
    abstract fun expensesDao(): DaoExpenses

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "PocketFlowDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}