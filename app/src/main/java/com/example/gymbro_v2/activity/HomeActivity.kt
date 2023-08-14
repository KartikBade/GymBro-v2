package com.example.gymbro_v2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.gymbro_v2.R
import com.example.gymbro_v2.adapter.ScheduleAdapter
import com.example.gymbro_v2.databinding.ActivityHomeBinding
import com.example.gymbro_v2.viewmodel.HomeViewModel
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
            }
            true
        }

        val scheduleAdapter = ScheduleAdapter(
            this,
            editScheduleClickListener = {
                val intent = Intent(this, EditScheduleActivity::class.java)
                intent.putExtra("scheduleId", it.scheduleId)
                intent.putExtra("oldScheduleName", it.scheduleName)
                intent.putExtra("oldScheduleDescription", it.scheduleDescription)
                intent.putExtra("oldScheduleDaysPlannedOn", it.scheduleDaysPlannedOn)
                startActivity(intent)
                finish()
            },
            scheduleClickListener = {
                val intent = Intent(this, ScheduleActivity::class.java)
                intent.putExtra("scheduleName", it.scheduleName)
                intent.putExtra("scheduleId", it.scheduleId)
                startActivity(intent)
                finish()
            }
        )

        binding.homeRvMain.adapter = scheduleAdapter
        homeViewModel.getAllSchedules().observe(this) {
            if (it.isEmpty()) {
                binding.homeTvEmptyRv.visibility = View.VISIBLE
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
}