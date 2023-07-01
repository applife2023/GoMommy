package com.example.gomommy

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.gomommy.databinding.ActivityHomepageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date
import java.util.Locale

class Homepage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityHomepageBinding
    private var backPressedOnce = false
    private var backPressedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.calendar -> replaceFragment(CalendarFragment())
                R.id.nearest_hospital -> replaceFragment(NearestHospitalFragment())
                R.id.tipsnadvice -> replaceFragment(TipsAndAdviceFragment())

                else -> {

                }
            }
            true

        }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toolbarIcon = findViewById<ImageView>(R.id.toolbar_icon)
        toolbarIcon.setImageResource(R.drawable.ic_current_date)

        val toolbarDate = findViewById<TextView>(R.id.toolbar_date)
        val currentDate = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(Date())
        toolbarDate.text = currentDate

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.visibility = View.VISIBLE
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, HomeFragment()).commit()
//            navigationView.setCheckedItem(R.id.nav_home)
//        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        // Loop through each menu item and set only icons to be displayed
        for (i in 0 until bottomNavigationView.menu.size()) {
            val menuItem = bottomNavigationView.menu.getItem(i)
            menuItem.title = "" // Set empty string to hide the title
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (backPressedCount == 1) {
                super.onBackPressed() // Exit the app
            } else {
                backPressedCount++

                val toast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0) // Set toast position
                toast.show()

                // Reset the back pressed count after a certain duration (e.g., 2 seconds)
                Handler(Looper.getMainLooper()).postDelayed({
                    backPressedCount = 0
                }, 2000)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment()).commit()
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment()).commit()
            R.id.nav_about -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutFragment()).commit()
            R.id.nav_logout -> userLogout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun userLogout() {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
        firebaseAuth.signOut()
        val intent = Intent(this@Homepage, LoginAccount::class.java)
        startActivity(intent)
        finish()
    }

}