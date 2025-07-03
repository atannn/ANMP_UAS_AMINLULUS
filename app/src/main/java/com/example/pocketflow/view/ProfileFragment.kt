package com.example.pocketflow.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentProfileBinding
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sharedPref = SharedPref(requireContext())

        val idUser = sharedPref.getUserId()

        userViewModel.getUserData(idUser)
        observeViewModel()

//        binding.btnLogout.setOnClickListener {
//            sharedPref.logout()
//            val intent = Intent(requireActivity(), LoginActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
//        }

    }
    fun observeViewModel(){
        userViewModel.selectedUser.observe(viewLifecycleOwner, Observer {
            binding.tvFirstName.setText(it.firstname)
            binding.tvLastName.setText(it.lastname)
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}