package com.example.gymbro_v2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gymbro_v2.R
import com.example.gymbro_v2.activity.AuthActivity
import com.example.gymbro_v2.databinding.FragmentLoginBinding
import com.example.gymbro_v2.databinding.FragmentSignupBinding
import com.example.gymbro_v2.viewmodel.AuthViewModel

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        authViewModel = (activity as AuthActivity).authViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            val email = binding.signupEtEmail.text.toString().trim()
            val password = binding.signupEtPassword.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()
            ) {
                authViewModel.createNewUser(email, password)
            }
        }
    }
}