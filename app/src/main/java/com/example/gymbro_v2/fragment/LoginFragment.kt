package com.example.gymbro_v2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gymbro_v2.R
import com.example.gymbro_v2.activity.AuthActivity
import com.example.gymbro_v2.activity.HomeActivity
import com.example.gymbro_v2.databinding.FragmentLoginBinding
import com.example.gymbro_v2.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        authViewModel = (activity as AuthActivity).authViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginTvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.loginEtEmail.text.toString().trim()
            val password = binding.loginEtPassword.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()
            ) {
                authViewModel.loginUser(email, password)
                if (
                    !activity?.getSharedPreferences(getString(R.string.user_shared_pref), 0)
                        ?.getString(getString(R.string.user_shared_pref_username), null).isNullOrBlank()
                ) {
                    val intent = Intent(activity?.applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }
}