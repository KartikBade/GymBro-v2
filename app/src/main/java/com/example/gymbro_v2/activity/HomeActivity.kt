package com.example.gymbro_v2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import coil.load
import com.example.gymbro_v2.R
import com.example.gymbro_v2.adapter.ScheduleAdapter
import com.example.gymbro_v2.databinding.ActivityHomeBinding
import com.example.gymbro_v2.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.homeDrawerLayout, R.string.open, R.string.close)
        binding.homeDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.homeNavView.getHeaderView(0).findViewById<TextView>(R.id.tv_email).text = homeViewModel.getUserEmail()
        binding.homeNavView.getHeaderView(0).findViewById<TextView>(R.id.tv_profile_photo).text = homeViewModel.getProfilePhotoLetter()

        binding.homeNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_drawer_logout -> {
                    homeViewModel.logout()
                    if (
                        getSharedPreferences(getString(R.string.user_shared_pref), 0)
                            .getString(getString(R.string.user_shared_pref_username), null).isNullOrBlank()
                    ) {
                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                R.id.home_drawer_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        val scheduleAdapter = ScheduleAdapter(
            scheduleClickListener = {
                val intent = Intent(this, ScheduleActivity::class.java)
                intent.putExtra("scheduleId", it.scheduleId)
                intent.putExtra("scheduleName", it.scheduleName)
                intent.putExtra("scheduleDescription", it.scheduleDescription)
                intent.putExtra("scheduleDaysPlannedOn", it.scheduleDaysPlannedOn)
                startActivity(intent)
            }
        )

        binding.homeRvMain.adapter = scheduleAdapter
        homeViewModel.getAllSchedules().observe(this) {
            if (it.isEmpty()) {
                binding.homeTvEmptyRv.visibility = View.VISIBLE
                scheduleAdapter.submitList(emptyList())
                return@observe
            } else {
                binding.homeTvEmptyRv.visibility = View.GONE
            }
            scheduleAdapter.submitList(it)
        }

        binding.homeFabAddSchedule.setOnClickListener {
            val intent = Intent(this, AddScheduleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}