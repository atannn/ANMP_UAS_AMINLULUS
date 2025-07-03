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
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.viewmodel.BudgetViewModel
import com.example.pocketflow.viewmodel.UserViewModel

class BudgetFragment : Fragment() {
    private lateinit var binding : FragmentBudgetBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var sharedPref: SharedPref
    private val budgetListAdapter  = BudgetAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        sharedPref = SharedPref(requireContext())

        val idUser = sharedPref.getUserId()

        budgetViewModel.getAllBudget(idUser)

        binding.recBudget.layoutManager = LinearLayoutManager(context)
        binding.recBudget.adapter = budgetListAdapter

        binding.btnAddBudget.setOnClickListener {
            val action = BudgetFragmentDirections.actionToAddBudget()
            Navigation.findNavController(it).navigate(action)
        }
        observeViewModel()
    }

    fun observeViewModel() {
        budgetViewModel.dataBudget.observe(viewLifecycleOwner, Observer {
            budgetListAdapter.updateListBudget(it)
            if (it.isEmpty()) {
                binding.recBudget?.visibility = View.GONE
            } else {
                binding.recBudget?.visibility = View.VISIBLE
            }
        })
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BudgetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}