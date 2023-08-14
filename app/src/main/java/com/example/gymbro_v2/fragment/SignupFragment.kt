package com.example.gymbro_v2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gymbro_v2.R
import com.example.gymbro_v2.activity.AuthActivity
import com.example.gymbro_v2.activity.HomeActivity
import com.example.gymbro_v2.databinding.FragmentSignupBinding
import com.example.gymbro_v2.model.User
import com.example.gymbro_v2.viewmodel.AuthViewModel

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        authViewModel = (activity as AuthActivity).authViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            val email = binding.signupEtEmail.text.toString().trim()
            val password = binding.signupEtPassword.text.toString().trim()
            val username = binding.signupEtUsername.text.toString().trim()
            val phone = binding.signupEtPhone.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()
            ) {
                authViewModel.signupUser(User(email, username, phone), password)
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