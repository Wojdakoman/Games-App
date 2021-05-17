package com.example.gameapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.gameapp.R
import com.example.gameapp.viewmodels.SharedViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //set up navigation
        navController = findNavController(R.id.fragment)
        bottom_navigation.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController)
        //bottom navigation handler
        bottom_navigation.setOnNavigationItemSelectedListener {
            val args = Bundle()
            when(it.title){
                getString(R.string.favourite) -> args.putInt("view", 1)
                getString(R.string.played) -> args.putInt("view", 0)
            }
            navController.navigate(it.itemId, args)
            true
        }
        //toolbar titles handler
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = when(destination.id){
                R.id.gameFragment -> arguments?.get("gameName").toString()
                R.id.searchResultsFragment -> getString(R.string.searchResults)
                R.id.gamesListFragment -> if(arguments?.get("view").toString().toInt() == 0) getString(R.string.played) else getString(R.string.favourite)
                else -> getString(R.string.app_name)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}