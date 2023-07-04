package com.example.musicplayer32.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayer32.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MusicPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val controller = findNavController(R.id.nav_host_fragment2)
        requestPermission()
        val fragmentSet = setOf<Int>(
            R.id.musicFragment
        )


        setupActionBarWithNavController(controller, AppBarConfiguration(fragmentSet))
        bottomNavView.setupWithNavController(controller)
    }
    private fun hasPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        val permission = mutableListOf<String>()

        if(!hasPermission()){
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }
        if (permission.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permission.toTypedArray(),0)
        }
    }




}