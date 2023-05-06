package com.example.gymbro_v2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.gymbro_v2.R
import com.example.gymbro_v2.databinding.ActivityAuthBinding
import com.example.gymbro_v2.repository.AuthRepository
import com.example.gymbro_v2.viewmodel.AuthViewModel
import com.example.gymbro_v2.viewmodel.AuthViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository
    private lateinit var binding: ActivityAuthBinding
    private lateinit var navController: NavController
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.authFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        val authViewModelProviderFactory = AuthViewModelProviderFactory(authRepository)
        authViewModel = ViewModelProvider(this, authViewModelProviderFactory)[AuthViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = getSharedPreferences(getString(R.string.user_shared_pref), 0)
            .getString(getString(R.string.user_shared_pref_username), null)

        currentUser?.let {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}