package com.example.gameapp.views.loginFragments

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.navigation.findNavController
import com.example.gameapp.R
import com.example.gameapp.views.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
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
        //sign in button
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
        //sign up button
        btnSignUp.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        //reset button
        btnResetPassword.setOnClickListener {
            showResetPassDialog()
        }
    }

    private fun login(login: String, pass: String){
        auth.signInWithEmailAndPassword(login, pass).addOnCompleteListener(requireActivity()) {
            if(it.isSuccessful){
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                val msg = when(it.exception){
                    is FirebaseAuthInvalidCredentialsException -> getString(R.string.wrongLoginPassword)
                    is FirebaseAuthInvalidUserException -> getString(R.string.userNotFound)
                    else -> "unknown"
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showResetPassDialog(){
        val editText = TextInputEditText(requireContext())
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.resetPassword))
                .setMessage(getString(R.string.email))
                .setView(editText)
                .setNegativeButton(getString(R.string.cancel)){ dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.resetPassword)){ dialog, which ->
                    Firebase.auth.sendPasswordResetEmail(editText.text?.toString())
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful)
                                    Toast.makeText(requireContext(), getString(R.string.passwordSend), Toast.LENGTH_SHORT).show()
                                else Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                    dialog.dismiss()
                }
                .show()
    }
}