package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mobile.api.model.Utilizador
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okhttp3.internal.Util

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            //get username
            val username = findViewById<EditText>(R.id.username).text.toString()
            val usernameField = findViewById<EditText>(R.id.username)
            //get Email
            val email = findViewById<EditText>(R.id.email).text.toString()
            val emailField = findViewById<EditText>(R.id.email)
            //get Password
            val password = findViewById<EditText>(R.id.password).text.toString()
            val passwordField = findViewById<EditText>(R.id.password)

            //Validar se os campos nao estao vazios
            if(email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                            val databaseRef = database.reference.child("utilizadores").child(firebaseAuth.currentUser!!.uid)
                            val utilizadores : Utilizador = Utilizador(username, firebaseAuth.currentUser!!.uid)

                            databaseRef.setValue(utilizadores).addOnCompleteListener {
                                if(it.isSuccessful) {
                                    //redirecionar para a Home Page
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else {
                                    val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
                                    alertDialog.setMessage(it.exception.toString())
                                    alertDialog.setNeutralButton("OK", null)
                                    alertDialog.show()
                                }
                            }
                        }else{
                            //caso apenas um dos campos esteje a faltar
                            if(email.isEmpty()){
                                emailField.error = "Por favor preencha o seu email"
                            }else if(password.isEmpty()){
                                passwordField.error = "Por favor preencha a sua password"
                            }else if(username.isEmpty()){
                                usernameField.error = "Por favor preencha o seu username"
                            }else if(password.length < 6){
                                passwordField.error = "A password precisa de pelo menos 6 caracteres"
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


    fun redirectToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}