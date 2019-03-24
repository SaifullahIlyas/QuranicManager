package com.example.quranicmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class ViewAyahList : AppCompatActivity() {
lateinit var surah:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ayah_list)
        val intent = getIntent()

        surah = intent.getStringExtra("surahname")
        getAllDocs()
    }
    private fun getAllDocs() {
        var i = 0;
        val array = ArrayList<String>()
        val list = findViewById<ListView>(R.id.ayahList)
        // [START get_multiple_all]
        val db = FirebaseFirestore.getInstance()
        db.collection("parah30").document(surah).collection("ayah")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    print( "${document.id} => ${document.data}"+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    array.add("${document.id}".toString())
                    //Toast.makeText(this,"${document.id}",Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->

            }.addOnCompleteListener{
                try {
                    array.sort()
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, array)
                    list.adapter = adapter

                    }
                catch (e:Exception)
                {
                    Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
                }

            }

        // [END get_multiple_all]
    }
}
