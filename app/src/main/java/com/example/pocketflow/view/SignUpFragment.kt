package com.example.pocketflow.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentSignUpBinding
import com.example.pocketflow.model.User
import com.example.pocketflow.viewmodel.UserViewModel


class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            val fname = binding.txtFirstname.text.toString()
            val lname = binding.txtLastname.text.toString()
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            if (fname.isBlank() || lname.isBlank() || username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Please Fill All The Blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                username = username,
                firstname = fname,
                lastname = lname,
                password = password
            )

            userViewModel.registerUser(user)
        }
        binding.btnToSignIn.setOnClickListener {
            val action = SignUpFragmentDirections.actionToSignInFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.signupResult.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (message == "Sign Up Succesfully") {
                findNavController().popBackStack()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}