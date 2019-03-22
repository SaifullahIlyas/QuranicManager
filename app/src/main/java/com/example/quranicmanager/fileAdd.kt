package com.example.quranicmanager

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_file_add.*

class fileAdd : AppCompatActivity() {
    val AUDIO : Int = 0
    var checkCode:Int  = 0
    lateinit var uri:Uri
    lateinit var downloadString:String
    override fun onCreate(savedInstanceState: Bundle?) {
        val parahTf: EditText?
        val ayahTf : EditText?
          val spinner: Spinner?

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_add)
        val uploadBtn = findViewById(R.id.uploadBtn) as? Button
        val parahradio = findViewById(R.id.parahRadio) as? RadioButton
         parahTf = findViewById(R.id.parahTf) as? EditText
         ayahTf = findViewById(R.id.ayahTf) as? EditText
        spinner = findViewById(R.id.surahMenu) as? Spinner
        if(parahradio!!.isChecked)
        {
            ayahTf!!.isEnabled = false
           parahTf!!.isEnabled = false
                   spinner!!.isEnabled = false

        }

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            R.layout.spinnerlayout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner!!.adapter = adapter
        }
    }
    fun oploadBtnClick(view: View)
    {
        try {
            Toast.makeText(this,"Upload clicked",Toast.LENGTH_LONG).show()
            upload()
        }
        catch (e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }

    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.parahRadio ->
                    if (checked) {
                        this.ayahTf!!.isEnabled = false
                       this.parahTf!!.isEnabled = false
                        val sss =    findViewById(R.id.surahMenu) as? Spinner
                        sss!!.isEnabled = false
                        checkCode=0

                    }
                R.id.surahRadio ->
                    if (checked) {

                     val sss =    findViewById(R.id.surahMenu) as? Spinner
                        sss!!.isEnabled = true
 checkCode = 1
                    }
                R.id.ayahRadio ->
                    if (checked) {
                        val sss =    findViewById(R.id.surahMenu) as? Spinner
                        sss!!.isEnabled = true
                        this.ayahTf.isEnabled = true
                        checkCode = 2
                    }
            }
        }
    }
    fun browseFileFOrUpload(view: View){
        val intent = Intent()
        intent.setType("audio/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"Select file"), AUDIO)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fileUrl = findViewById<TextView>(R.id.selectedFileView)
        if (resultCode == RESULT_OK) {
            if (requestCode == AUDIO) {
                uri  = data!!.data
                fileUrl.text = uri.lastPathSegment.toString()
                /// upload ()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
   private fun upload() {
       val alertDialogBuilder = AlertDialog.Builder(this)

// set title
       alertDialogBuilder.setTitle("Uploading!!!!!!!!!!!!!!!!")

// set dialog message


// create alert dialog
       val alertDialog = alertDialogBuilder.create()

// show it
       alertDialog.show()
       var metadata = StorageMetadata.Builder()
           .setContentType("audio/mp3")
           .build()
      val mReference = createStorageRefrence()

        try {
            mReference.putFile(uri,metadata).addOnSuccessListener {
                Toast.makeText(this, "Successfully Uploaded !!!!", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{

            }.addOnPausedListener {

            }.addOnCanceledListener {

            }.addOnProgressListener {taskSnapshot->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                alertDialog.show()
                alertDialogBuilder.setMessage(progress.toInt().toString()+"%").setCancelable(false)


                //Toast.makeText(this, progress.toInt().toString()+"%", Toast.LENGTH_LONG).show()
            }.addOnCompleteListener{
                val downloadlink = mReference.downloadUrl
                uploadInDatabase(downloadlink.toString())
                alertDialog.dismiss()

            }

        }catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
    fun uploadInDatabase(
        link:String
    )
    {  val spinner  = findViewById(R.id.surahMenu) as Spinner
        val ayahnumber =  findViewById<EditText>(R.id.ayahTf)
        val databs = FirebaseFirestore.getInstance()
        if (checkCode == 0)
        {
            val linkval = HashMap<String, Any>()
            linkval["link"] = link.toString()
            linkval["storageRef"] = downloadString.toString()


            databs.collection("parah30").document("parah30")
                .set(linkval)


        }
        else if (checkCode == 1)
        {
            try {

                val linkval = HashMap<String, Any>()
                linkval["link"] = link.toString()
                linkval["storageRef"] = downloadString.toString()

                databs.collection("parah30").document(spinner.selectedItem.toString() )
                    .set(linkval)

            }
            catch (e:Exception)
            {
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }

        }
        else if(checkCode == 2){
            val ayahTf = findViewById<EditText>(R.id.ayahTf)
            val ayahDoc = ayahTf.text.toString().toInt()
            try {

                val linkval = HashMap<String, Any>()
                linkval["link"] = link.toString()
                linkval["storageRef"] = downloadString.toString()


                databs.collection("parah30").document(spinner.selectedItem.toString() ).collection("ayah").document(ayahDoc.toString())
                    .set(linkval)

            }
            catch (e:Exception)
            {
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }

        }
    }
    fun  createDocument()
    {

    }
    fun createStorageRefrence(): StorageReference {  val ayahTf = findViewById<EditText>(R.id.ayahTf)
        val spinner  = findViewById(R.id.surahMenu) as Spinner
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        var mReference :StorageReference=storage.reference.child("parah30")
        if(checkCode==0)
        {
            mReference  = storage.reference.child("parah30")
            downloadString = "parah30"
        }
       else if(checkCode==1)
        { downloadString = "parah30/"+spinner.selectedItem.toString()
            mReference  = storage.reference.child("parah30/"+spinner.selectedItem.toString())
        }
        else if(checkCode==2)
        {
            downloadString = "parah30/"+spinner.selectedItem.toString()+"/"+ayahTf.text.toString().toInt().toString()
            mReference  = storage.reference.child("parah30/"+spinner.selectedItem.toString()+"/"+ayahTf.text.toString().toInt().toString())
        }
        return mReference
    }



}
