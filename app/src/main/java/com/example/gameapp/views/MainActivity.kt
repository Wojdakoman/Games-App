package com.example.gameapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gameapp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var auth = Firebase.auth
        if(auth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        setContentView(R.layout.activity_main)
    }
}