package com.example.pocketflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pocketflow.model.AppDatabase
import com.example.pocketflow.model.Budget
import com.example.pocketflow.model.Expenses
import com.example.pocketflow.model.ExpensesWithBudget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ExpensesViewModel (application: Application) : AndroidViewModel(application),
    CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val daoExpenses = AppDatabase.getDatabase(application).expensesDao()
    private val daoBudget = AppDatabase.getDatabase(application).budgetDao()

    val dataExpenses = MutableLiveData<List<ExpensesWithBudget>>()
    val selectedExpenses = MutableLiveData<ExpensesWithBudget>()

    fun getAllExpenses(idUser: Int){
        launch {
            val db = AppDatabase.getDatabase(
                getApplication()
            )
            dataExpenses.postValue(db.expensesDao().getExpensesWithBudget(idUser))
        }
    }
    fun addExpenses(expenses: Expenses){
        viewModelScope.launch(Dispatchers.IO) {
            daoExpenses.insertExpenseAndUpdateBudget(expenses)
        }
    }
    fun getSelectedExpenses(id: Int){
        launch {
            val db = AppDatabase.getDatabase(getApplication())
            selectedExpenses.postValue(db.expensesDao().getSelectedExpenses(id))
        }
    }
}