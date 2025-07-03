package com.example.pocketflow.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentBudgetBinding
import com.example.pocketflow.databinding.FragmentReportBinding
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.viewmodel.BudgetViewModel
import com.example.pocketflow.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.Locale

class ReportFragment : Fragment() {
    private lateinit var binding : FragmentReportBinding
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var sharedPref: SharedPref
    private val reportListAdapter  = ReportAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        sharedPref = SharedPref(requireContext())

        val idUser = sharedPref.getUserId()

        budgetViewModel.getAllBudget(idUser)

        binding.recReport.layoutManager = LinearLayoutManager(context)
        binding.recReport.adapter = reportListAdapter

        observeViewModel()
    }

    fun observeViewModel() {
        budgetViewModel.dataBudget.observe(viewLifecycleOwner, Observer { budgetList ->
            reportListAdapter.updateListBudget(budgetList)

            if (budgetList.isEmpty()) {
                binding.recReport.visibility = View.GONE
            } else {
                binding.recReport.visibility = View.VISIBLE

                // Hitung total nominal dan total budgetLeft
                val totalBudget = budgetList.sumOf { it.nominal }
                val totalLeftBudget = budgetList.sumOf { it.budgetLeft }
                val totalUsage = totalBudget - totalLeftBudget

                binding.tvTotalBudgetSummary.text = formatRupiah(totalUsage) + " / " + formatRupiah(totalBudget)
                binding.totalBudgetProgressBar.max = totalBudget
                binding.totalBudgetProgressBar.progress = totalUsage
            }
        })
    }
    fun formatRupiah(nominal: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp${formatter.format(nominal)}"
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}