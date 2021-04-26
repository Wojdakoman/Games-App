package com.example.gameapp.views.loginFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gameapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        auth.signInWithEmailAndPassword(login, pass).addOnCompleteListener(requireActivity()) {
            if(it.isSuccessful){
                Toast.makeText(context, "Zalogowano!", Toast.LENGTH_SHORT).show()
            } else {
                var msg = ""
                when(it.exception){
                    is FirebaseAuthInvalidCredentialsException -> msg = "Podano nieprawidłowy login"
                    is FirebaseAuthInvalidUserException -> msg = "Brak użytkownika"
                    else -> msg = "unknown"
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}