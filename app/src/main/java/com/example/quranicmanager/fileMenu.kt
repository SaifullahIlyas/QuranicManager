package com.example.quranicmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class fileMenu:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)
        val addOption = findViewById<CardView>(R.id.addBtn)
        val viewOption = findViewById<CardView>(R.id.viewBtn)
        val delOption = findViewById<CardView>(R.id.delBtn)
        Toast.makeText(this,addOption!!.id,Toast.LENGTH_LONG).show()
        print("============================================================================="+addOption!!.id)
        addOption!!.setOnClickListener {
val inetentaddfile = Intent(this,fileAdd::class.java)
            startActivity(inetentaddfile)
        }
        delOption!!.setOnClickListener {
            Toast.makeText(this,"cardClicked",Toast.LENGTH_LONG).show()
        }
        viewOption!!.setOnClickListener {
            val intent = Intent(this,FileView::class.java)
            startActivity(intent);
        }
}
}