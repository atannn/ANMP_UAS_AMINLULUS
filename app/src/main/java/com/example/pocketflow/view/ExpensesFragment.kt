package com.example.pocketflow.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentBudgetBinding
import com.example.pocketflow.databinding.FragmentExpensesBinding
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.viewmodel.BudgetViewModel
import com.example.pocketflow.viewmodel.ExpensesViewModel
import com.example.pocketflow.viewmodel.UserViewModel


class ExpensesFragment : Fragment() {
    private lateinit var binding : FragmentExpensesBinding
    private lateinit var expensesViewModel: ExpensesViewModel
    private val expensesListAdapter  = ExpensesAdapter(arrayListOf())
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expensesViewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]
        sharedPref = SharedPref(requireContext())

        val idUser = sharedPref.getUserId()

        expensesViewModel.getAllExpenses(idUser)

        //setup recycle view (vertical) dan menghubungkan adapter
        binding.recExpenses.layoutManager = LinearLayoutManager(context)
        binding.recExpenses.adapter = expensesListAdapter

        //navigasi ke addexpenses
        binding.btnAddExpense.setOnClickListener {
            val action = ExpensesFragmentDirections.actionToAddExpenses()
            Navigation.findNavController(it).navigate(action)
        }
        observeViewModel()
    }

    //kalo ada data, rcview ditampilkan, jika tidak disembunyikan
    fun observeViewModel() {
        expensesViewModel.dataExpenses.observe(viewLifecycleOwner, Observer {
            expensesListAdapter.updateListBudget(it)
            if (it.isEmpty()) {
                binding.recExpenses?.visibility = View.GONE
            } else {
                binding.recExpenses?.visibility = View.VISIBLE
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpensesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}