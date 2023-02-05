package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobile.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        replaceFragment(HomeFragment())

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //Substiutir o fragment consoante o id selecionado no bottom nav view
        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.perfil -> replaceFragment(PerfilFragment())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.about -> replaceFragment(AboutFragment())
                R.id.lerMaisTardeMenu -> replaceFragment(LerMaisTardeFragment())
                else -> {
                    //
                }

            }
            true
        }
    }

    public fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.commit()
    }
}

