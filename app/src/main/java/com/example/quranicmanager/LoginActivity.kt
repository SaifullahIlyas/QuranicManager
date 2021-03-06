package com.example.quranicmanager

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
lateinit var user:EditText;
    lateinit var password:EditText
    lateinit var alert:AlertDialog.Builder;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        user   = findViewById(R.id.adminusername) as EditText
        password = findViewById(R.id.adminPassword) as EditText
        alert  = AlertDialog.Builder(this);
        val alertDialog = alert.create();


        val loginBtn = findViewById<Button>(R.id.signinBtn)
        loginBtn.setOnClickListener{
            var view = it;

            val Auth = FirebaseAuth.getInstance()

                     it.isEnabled = false
                alertDialog.setTitle("Credential Error")
            alertDialog.setCancelable(true);
            alertDialog.setIcon(R.drawable.credential)
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"ok",DialogInterface.OnClickListener { dialog, which ->
                alertDialog.hide();
            })

            if(user.text.length<1) {
                alertDialog.setMessage("Email Required!!")
                alertDialog.show()
                it.isEnabled = true;
            }

            if (password.text.length<1){
                alertDialog.setMessage("Password Required!!")
                alertDialog.show()
                it.isEnabled = true;
                Log.d("email", "no value found")
            }

               // alert = AlertDialog(this,"mmamamma",)

            if(!user.text.isEmpty() && !password.text.isEmpty() )
            {
                Auth.signInWithEmailAndPassword(user.text.toString().decapitalize(),password.text.toString()).addOnSuccessListener {
                    val intent = Intent(this,Home::class.java)
                    intent.putExtra("userEmail",user.text.toString().decapitalize())
                    startActivity(intent)
                    finish();
                    view.isEnabled = true

                }.addOnFailureListener{

                    alertDialog.setMessage(it.localizedMessage)
                    alertDialog.show()
                    view.isEnabled = true;
                }

            }


                //alert
            //Auth.signInWithEmailAndPassword()
           /* val intent = Intent(this,Home::class.java)
            startActivity(intent)*/

        }
        val registerBtn = findViewById<Button>(R.id.nothaveBtn)
        registerBtn.setOnClickListener{

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
