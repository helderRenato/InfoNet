package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mobile.model.Utilizador
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
            //get Email
            val email = findViewById<EditText>(R.id.email).text.toString()
            //get Password
            val password = findViewById<EditText>(R.id.password).text.toString()

            //Validar se os campos nao estao vazios
            if(email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    val databaseRef = database.reference.child("utilizadores").child(firebaseAuth.currentUser!!.uid)
                    val utilizadores : Utilizador = Utilizador(username, firebaseAuth.currentUser!!.uid)

                    databaseRef.setValue(utilizadores).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Tente Novamente, Algo correu mal !", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Preencha os campos em falta!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun redirectToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}