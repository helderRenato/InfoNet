package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobile.fragment.HomeFragment


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        replaceFragment(HomeFragment())
    }
}

private fun replaceFragment(fragment: Fragment){
    val fragmentManager = supportFragmentManager
    val fragmentTransition = fragmentManager.beginTransaction()
    fragmentTransition.replace(R.id.frame_layout, fragment)
    fragmentTransition.commit()
}