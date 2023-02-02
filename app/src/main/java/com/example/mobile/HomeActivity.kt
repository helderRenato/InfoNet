package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobile.fragment.HomeFragment
import com.example.mobile.fragment.PerfilFragment
import com.example.mobile.fragment.SearchFragment
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
                else -> {
                    //
                }

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.commit()
    }
}

