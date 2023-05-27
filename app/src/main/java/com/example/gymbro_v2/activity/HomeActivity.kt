package com.example.gymbro_v2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import com.example.gymbro_v2.R
import com.example.gymbro_v2.adapter.ScheduleAdapter
import com.example.gymbro_v2.databinding.ActivityHomeBinding
import com.example.gymbro_v2.model.Schedule
import com.example.gymbro_v2.repository.UserRepository
import com.example.gymbro_v2.viewmodel.UserViewModel
import com.example.gymbro_v2.viewmodel.UserViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository
    lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userViewModelProviderFactory = UserViewModelProviderFactory(userRepository)
        userViewModel = ViewModelProvider(this, userViewModelProviderFactory)[UserViewModel::class.java]

        toggle = ActionBarDrawerToggle(this, binding.homeDrawerLayout, R.string.open, R.string.close)
        binding.homeDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.homeNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_drawer_logout -> {
                    userViewModel.logout()
                    if (
                        getSharedPreferences(getString(R.string.user_shared_pref), 0)
                            .getString(getString(R.string.user_shared_pref_username), null).isNullOrBlank()
                    ) {
                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            true
        }

        val exerciseAdapter = ScheduleAdapter(this) {
            userViewModel.deleteExercise(it)
        }
        binding.homeRvMain.adapter = exerciseAdapter
        userViewModel.getAllExercise().observe(this) {
            exerciseAdapter.submitList(it)
            if (it.isEmpty()) {
                binding.homeTvEmptyRv.visibility = View.VISIBLE
            } else {
                binding.homeTvEmptyRv.visibility = View.GONE
            }
        }

        binding.homeFabAddExercise.setOnClickListener {
            userViewModel.insertExercise(Schedule("Push", "Train your pushing muscles like the chest, triceps and shoulders.", "Mon, Thu"))
            userViewModel.insertExercise(Schedule("Pull", "Train your pulling muscles like the back and biceps.", "Tue, Sat"))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}