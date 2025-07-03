package com.example.pocketflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketflow.databinding.CardbudgetBinding
import com.example.pocketflow.databinding.CardreportBinding
import com.example.pocketflow.model.Budget
import com.example.pocketflow.view.BudgetAdapter.BudgetViewHolder
import java.text.NumberFormat
import java.util.Locale

class ReportAdapter (val listBudget:ArrayList<Budget>)
    : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
    class ReportViewHolder(var binding: CardreportBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        var binding = CardreportBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listBudget.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.binding.tvBudgetName.text = listBudget[position].name
        holder.binding.tvUsed.text = formatRupiah(listBudget[position].nominal - listBudget[position].budgetLeft)
        holder.binding.tvTotalBudget.text = formatRupiah(listBudget[position].nominal)
        holder.binding.budgetProgressBar.max = listBudget[position].nominal
        holder.binding.budgetProgressBar.progress = listBudget[position].nominal - listBudget[position].budgetLeft
        holder.binding.tvRemainingBalance.text = "budget left: " + formatRupiah(listBudget[position].budgetLeft)
    }
    fun updateListBudget(newTodoList: List<Budget>) {
        listBudget.clear()
        listBudget.addAll(newTodoList)
        notifyDataSetChanged()
    }
    fun formatRupiah(nominal: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp${formatter.format(nominal)}"
    }


}