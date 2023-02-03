package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mobile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val button = findViewById<Button>(R.id.iniciarSessao)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        button.setOnClickListener{
            //get Email
            val email = findViewById<EditText>(R.id.email1).text.toString()
            val emailField = findViewById<EditText>(R.id.email1)
            //get Password
            val password = findViewById<EditText>(R.id.password1).text.toString()
            val passwordField = findViewById<EditText>(R.id.password1)

            if(email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        if(email.isEmpty()){
                            emailField.error = "Por favor preencha o email"
                        }else if(password.isEmpty()){
                            passwordField.error = "Por favor preencha a password"
                        }

                        val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
                        alertDialog.setMessage(it.exception.toString())
                        alertDialog.setNeutralButton("OK", null)
                        alertDialog.show()
                    }
                }
            }

        }
    }

    fun redirectToRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}