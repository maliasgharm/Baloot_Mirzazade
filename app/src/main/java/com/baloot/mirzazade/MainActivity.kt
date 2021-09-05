package com.baloot.mirzazade

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.baloot.mirzazade.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var appBarConfiguration : AppBarConfiguration? = null
    private var flagSplash = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!flagSplash){
            flagSplash = true
            binding.layerSplash.isVisible = true
            binding.layerContent.x = -1000f
            binding.tvSplash.x = -500f
            binding.layerContent.animate().x(0f).setStartDelay(1000).setDuration(500).start()
            binding.tvSplash.animate().x(0f).setStartDelay(1500).setDuration(500).start()
            binding.layerSplash.animate().x(3000f).setStartDelay(3000L).start()
            supportActionBar?.hide()
            Handler(Looper.getMainLooper()).postDelayed({
                supportActionBar?.show()
            },3500)
        }else{
            binding.layerSplash.isVisible = false
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_news, R.id.nav_profile, R.id.nav_marked
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration!!)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            navView.isVisible = destination.id == R.id.nav_news || destination.id == R.id.nav_profile || destination.id == R.id.nav_marked
        }
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return appBarConfiguration?.let { navController.navigateUp(it) }?:false
                || super.onSupportNavigateUp()
    }
}