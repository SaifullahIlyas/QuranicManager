package com.example.quranicmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class feedbackdetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbackdetail)
        val intent = Intent()
        val received = intent.getStringExtra("userdata")
        Toast.makeText(this,received,Toast.LENGTH_LONG).show()
    }
    fun readData(message:String)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        val refrence = FirebaseFirestore.getInstance()
        val refr = refrence.collection("Feedback").document(message)
        refr.get().addOnSuccessListener {document->
            if(document !=null)
            {
                Toast.makeText(this,"${document.data}"+"this is data",Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener{

        }.addOnCompleteListener{

        }
    }
}
