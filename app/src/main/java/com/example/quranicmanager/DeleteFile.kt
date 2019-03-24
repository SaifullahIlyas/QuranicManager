package com.example.quranicmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_file_add.*

class DeleteFile : AppCompatActivity() {
lateinit var  db:FirebaseFirestore
    lateinit var doc:String
    lateinit var parahTf: EditText
    lateinit var ayahTf : EditText
    lateinit var spinner: Spinner
 var checkCode:Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_file)
        parahTf = findViewById(R.id.parahTfDel) as EditText
        ayahTf = findViewById(R.id.ayahTfDel) as EditText
        spinner = findViewById(R.id.surahMenuDel) as Spinner
        val parahradio = findViewById(R.id.parahRadioDel) as RadioButton
        if(parahradio.isChecked)
        {
            ayahTf.isEnabled = false
            parahTf.isEnabled = false
            spinner.isEnabled = false

        }
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            R.layout.spinnerlayout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    fun delBtnClick(view: View)
    {
deleetFile()
    }
    fun onRadioButtonClickedDel(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.parahRadioDel ->
                    if (checked) {
                        this.ayahTf.isEnabled = false
                        this.parahTf.isEnabled = false
                        val sss =    findViewById(R.id.surahMenuDel) as? Spinner
                        sss!!.isEnabled = false
                        checkCode=0

                    }
                R.id.surahRadioDel ->
                    if (checked) {

                        val sss =    findViewById(R.id.surahMenuDel) as Spinner
                        sss.isEnabled = true
                        checkCode = 1
                    }
                R.id.ayahRadioDel ->
                    if (checked) {
                        val sss =    findViewById(R.id.surahMenuDel) as Spinner
                        sss.isEnabled = true
                        this.ayahTf.isEnabled = true
                        checkCode = 2
                    }
            }
        }
    }


    fun  deleetFile() {
        db = FirebaseFirestore.getInstance()
        var ref = db.collection("parah30").document()
        if (checkCode == 1) {
            ref = db.collection("parah30").document(spinner.selectedItem.toString())
        } else if (checkCode == 2 )
        {
            ref = db.collection("parah30").document(spinner.selectedItem.toString()).collection("ayah").document(ayahTf.text.toString())
        }
        ref.delete();
    }
}
