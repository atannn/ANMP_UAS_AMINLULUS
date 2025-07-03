package com.example.pocketflow.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentSignInBinding
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.viewmodel.UserViewModel

class SignInFragment : Fragment() {
    private lateinit var binding : FragmentSignInBinding
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
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnSignIn.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Harap isi semua field", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.login(username, password)
            }
        }
        binding.btnToSignUp.setOnClickListener {
            val action = SignInFragmentDirections.actionToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }
    private fun observeViewModel() {
        userViewModel.signinResult.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

                if (it == "Login Succesfully") {
                    // Simpan user ID ke session
                    userViewModel.loggedInUserId.value?.let { userId ->
                        sharedPref.saveLogin(userId)
                    }

                    // Pindah ke MainActivity
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                // Reset hasil login biar tidak ke-trigger ulang
                userViewModel.clearLoginResult()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}