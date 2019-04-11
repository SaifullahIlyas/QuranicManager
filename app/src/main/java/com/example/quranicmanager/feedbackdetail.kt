package com.example.quranicmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class feedbackdetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbackdetail)
        val intent = getIntent()
        val received = intent.getStringExtra("userdata")
        readData(received)
    }
    fun readData(message:String)
    {
        val refrence = FirebaseFirestore.getInstance()
        val refr = refrence.collection("Feedback").document(message)
        refr.get().addOnSuccessListener {document->
            if(document !=null)
            {
               val date = findViewById<TextView>(R.id.dateTV)
                val message =  findViewById<TextView>(R.id.feebackTV)
                date.text = document["Date"].toString()
                message.text = document["feedBack"].toString()
            }

        }.addOnFailureListener{

        }.addOnCompleteListener{

        }
    }
}
