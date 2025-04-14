package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            login(email, password)
        }

        signupButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            signUp(email, password)
        }
    }

    private fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please fill in all fields")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Login Successful 🌼")
                    navigateToDashboard() // Navigate to DashboardActivity
                } else {
                    showToast("Login Failed: ${it.exception?.message}")
                }
            }
    }

    private fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please fill in all fields")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Signup Successful 🎉")
                    navigateToDashboard() // Navigate to DashboardActivity
                } else {
                    showToast("Signup Failed: ${it.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java) // Navigate to DashboardActivity
        startActivity(intent)
        finish() // Finish MainActivity to prevent back navigation
    }
}
