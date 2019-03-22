package com.example.quranicmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener{
            Toast.makeText(this,"Login Validation code",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Home::class.java)
            startActivity(intent)

        }
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            Toast.makeText(this,"Here is the Register Code ",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }
}
