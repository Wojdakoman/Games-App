package com.example.gameapp.views.loginFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gameapp.R
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.btnSignUp
import kotlinx.android.synthetic.main.fragment_sign_up.txtUserLogin
import kotlinx.android.synthetic.main.fragment_sign_up.txtUserPass

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sign up button
        btnSignUp.setOnClickListener {
            var login: String = txtUserLogin.text.toString()
            var pass: String = txtUserPass.text.toString()
            var pass2: String = txtUserPass2.text.toString()

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

            if(pass2.isNullOrBlank()){
                txtUserPass2.error = getString(R.string.inputBlank)
                canProceed = false
            } else if(pass != pass2){
                txtUserPass2.error = getString(R.string.passwordDiffer)
                canProceed = false
            }
            else txtUserLogin.error = null

            if(canProceed) signUp(login, pass)
        }
    }

    private fun signUp(login: String, password: String){
        auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(requireActivity()){
            if(it.isSuccessful){
                Toast.makeText(context, "Zarejestrowano!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("[LOG]", "er: ${it.exception}")
                var msg = ""
                when(it.exception){
                    is FirebaseAuthWeakPasswordException-> msg = "Wybrane hasło jest za słabe"
                    is FirebaseAuthInvalidCredentialsException -> msg = "Podaj email jako login"
                    is FirebaseAuthUserCollisionException -> msg = "Konto już istnieje!"
                    else -> msg = "unknown"
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}