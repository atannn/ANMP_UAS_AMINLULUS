package com.example.pocketflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pocketflow.model.AppDatabase
import com.example.pocketflow.model.Budget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BudgetViewModel (application: Application) : AndroidViewModel(application),
    CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val daoBudget = AppDatabase.getDatabase(application).budgetDao()

    val dataBudget = MutableLiveData<List<Budget>>()
    val selectedBudget = MutableLiveData<Budget>()

    fun getAllBudget(idUser: Int){
        launch {
            val db = AppDatabase.getDatabase(
                getApplication()
            )
            dataBudget.postValue(db.budgetDao().getAllBudgets(idUser))
        }
    }

    fun getSelectedBudget(id: Int){
        launch {
            val db = AppDatabase.getDatabase(getApplication())
            selectedBudget.postValue(db.budgetDao().selectBudget(id))
        }
    }

    fun addBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            daoBudget.insert(budget)
        }
    }

    fun editBudget(pname: String, pnominal: Int, pbudgetLeft: Int, pid: Int){
        launch {
            val db = AppDatabase.getDatabase(getApplication())
            db.budgetDao().update(pname, pnominal, pbudgetLeft, pid)
        }
    }
}