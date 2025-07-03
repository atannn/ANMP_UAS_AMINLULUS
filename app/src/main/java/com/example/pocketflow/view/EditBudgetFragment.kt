package com.example.pocketflow.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentAddBudgetBinding
import com.example.pocketflow.model.Budget
import com.example.pocketflow.viewmodel.BudgetViewModel


class EditBudgetFragment : Fragment() {
    private lateinit var binding: FragmentAddBudgetBinding
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var budget: Budget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        binding.title.text = "Edit Budget"
        binding.btnSubmitBudget.text = "Save Change"

        // retrieve the argument from clicked+ imgEdit
        val id = EditBudgetFragmentArgs.fromBundle(requireArguments()).id
        budgetViewModel.getSelectedBudget(id)
        observeViewModel()
        binding.btnSubmitBudget.setOnClickListener {
            val name = binding.etBudgetName.text.toString()
            val nominal = binding.etBudgetNominal.text.toString().toInt()
            val spentBudget = budget.nominal - budget.budgetLeft

            if (spentBudget < nominal){
                val budgetEntity = Budget(
                    name = name,
                    nominal = nominal,
                    budgetLeft = nominal - spentBudget,
                    idUser = budget.idUser
                )
                budgetViewModel.editBudget(budgetEntity.name, budgetEntity.nominal, budgetEntity.budgetLeft, budget.id )
                Toast.makeText(requireContext(), "Edit Budget Succesfully", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack() // lebih clean
            } else {
                Toast.makeText(requireContext(), "Budget Nominal is under your expenses", Toast.LENGTH_LONG).show()
            }
//            Toast.makeText(requireContext(), budget.id.toString() +
//                " budget: " + budget.budget.toString() + " budgetleft: " + budget.budgetLeft.toString()
//                + " name: " + budget.name + " budgetspend: " + budget.budgetSpend.toString(), Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

    }
    fun observeViewModel() {
        budgetViewModel.selectedBudget.observe(viewLifecycleOwner, Observer {
            budget = it
            binding.etBudgetName.setText(it.name)
            binding.etBudgetNominal.setText(it.nominal.toString())
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditBudgetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}