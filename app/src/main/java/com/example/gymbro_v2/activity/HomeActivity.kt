package com.example.gymbro_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gymbro_v2.R
import com.example.gymbro_v2.repository.AuthRepository
import com.example.gymbro_v2.repository.UserRepository
import com.example.gymbro_v2.viewmodel.AuthViewModel
import com.example.gymbro_v2.viewmodel.AuthViewModelProviderFactory
import com.example.gymbro_v2.viewmodel.UserViewModel
import com.example.gymbro_v2.viewmodel.UserViewModelProviderFactory
import com.google.firebase.firestore.ktx.firestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userViewModelProviderFactory = UserViewModelProviderFactory(userRepository)
        userViewModel = ViewModelProvider(this, userViewModelProviderFactory)[UserViewModel::class.java]

        val sh = getSharedPreferences(getString(R.string.user_shared_pref), MODE_PRIVATE)
        val s1 = sh.getString(getString(R.string.user_shared_pref_username), "User")

        val tv = findViewById<TextView>(R.id.tv_demo)
        tv.text = s1.toString()
    }
}