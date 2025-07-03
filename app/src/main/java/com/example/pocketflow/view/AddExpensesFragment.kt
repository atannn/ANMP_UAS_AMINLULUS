package com.example.pocketflow.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.pocketflow.databinding.FragmentAddExpensesBinding
import com.example.pocketflow.model.Budget
import com.example.pocketflow.model.Expenses
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.viewmodel.BudgetViewModel
import com.example.pocketflow.viewmodel.ExpensesViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpensesFragment : Fragment() {
    private lateinit var binding: FragmentAddExpensesBinding
    private lateinit var expensesViewModel: ExpensesViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var selectedBudget: Budget

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expensesViewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        val sharedPref = SharedPref(requireContext())
        val userId = sharedPref.getUserId()

        budgetViewModel.getAllBudget(userId)
        observeViewModel()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        binding.etExpenseDate.setText(dateFormat.format(calendar.time))

        binding.etExpenseDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                binding.etExpenseDate.setText(dateFormat.format(calendar.time))
            }, year, month, day).show()
        }

        binding.btnSubmitExpense.setOnClickListener {
            val date = dateStringToLong(binding.etExpenseDate.text.toString())
            val idBudget = selectedBudget.id
            val nominalText = binding.etExpenseAmount.text.toString()
            val nominal = nominalText.toIntOrNull()
            val desc = binding.etExpenseDescription.text.toString()

            if (date == null || nominalText.isBlank() || nominal == null || nominal <= 0 || idBudget == null || desc.isBlank()) {
                Toast.makeText(requireContext(), "Please enter a valid positive number and fill all fields.", Toast.LENGTH_SHORT).show()
            } else if (nominal > selectedBudget.budgetLeft) {
                Toast.makeText(requireContext(), "Nominal is more than your remaining budget", Toast.LENGTH_SHORT).show()
            } else {
                val expensesEntity = Expenses(
                    date = date,
                    idBudget = idBudget,
                    nominal = nominal,
                    desc = desc,
                    idUser = userId
                )
                expensesViewModel.addExpenses(expensesEntity)
                Toast.makeText(requireContext(), "Expenses Added Successfully", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack()
            }
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun observeViewModel() {
        budgetViewModel.dataBudget.observe(viewLifecycleOwner, Observer { budgets ->
            budgets?.let {
                val budgetNames = it.map { budget -> budget.name }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, budgetNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerBudget.adapter = adapter

                binding.spinnerBudget.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        selectedBudget = budgets[position]
                        budgetViewModel.selectedBudget.value = selectedBudget
                        binding.tvRemaining.text = formatRupiah(selectedBudget.budgetLeft)
                        binding.tvTotalBudget.text = formatRupiah(selectedBudget.nominal)
                        binding.pbBudget.max = selectedBudget.nominal
                        binding.pbBudget.progress = selectedBudget.budgetLeft
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        budgetViewModel.selectedBudget.value = null
                    }
                }
            }
        })
    }

    private fun formatRupiah(nominal: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp${formatter.format(nominal)}"
    }

    private fun dateStringToLong(dateStr: String, pattern: String = "dd-MMM-yyyy"): Long {
        return try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val date = sdf.parse(dateStr)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}