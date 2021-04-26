package com.example.gameapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gameapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        btnLogin.setOnClickListener {
            var login: String = txtUserLogin.text.toString()
            var pass: String = txtUserPass.text.toString()

            var canProceed = true

            if(login.isNullOrBlank()){
                txtUserLogin.error = getString(R.string.inputBlank)
                canProceed = false
            }
            else txtUserLogin.error = null

            if(pass.isNullOrBlank()){
                txtUserPass.error = getString(R.string.inputBlank)
                canProceed = false
            }
            else txtUserLogin.error = null

            if(canProceed) login(login, pass)
        }
    }

    private fun login(login: String, pass: String){
        auth.signInWithEmailAndPassword(login, pass).addOnCompleteListener(this) {
            if(it.isSuccessful){
                Toast.makeText(this, "Zalogowano!", Toast.LENGTH_SHORT).show()
            } else {
                var msg = ""
                when(it.exception){
                    is FirebaseAuthInvalidCredentialsException -> msg = "Podano nieprawidłowy login"
                    is FirebaseAuthInvalidUserException -> msg = "Brak użytkownika"
                    else -> msg = "unknown"
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}