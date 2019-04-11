package com.example.quranicmanager

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
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
        val parahTf: Spinner?
        val ayahTf : EditText?
          val spinner: Spinner?
        val progressBar:ProgressBar?

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_add)
        val uploadBtn = findViewById(R.id.uploadBtn) as? Button
        val parahradio = findViewById(R.id.parahRadio) as? RadioButton
         parahTf = findViewById(R.id.parahTf) as? Spinner
        spinner = findViewById(R.id.surahMenu) as? Spinner
        progressBar =  findViewById(R.id.uploadprogrees) as? ProgressBar
        if(parahradio!!.isChecked)
        {

           parahTf!!.isEnabled = true
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
        ArrayAdapter.createFromResource(this,R.array.paraharray,R.layout.spinnerlayout).also { arrayAdapter: ArrayAdapter<CharSequence> ->
            parahTf!!.adapter = arrayAdapter;
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
   @RequiresApi(Build.VERSION_CODES.N)
   private fun upload() {
       val alertDialogBuilder = AlertDialog.Builder(this)

// set title
       alertDialogBuilder.setTitle("Uploading!!!!!!!!!!!!!!!!")

// set dialog message
val progressBar = findViewById(R.id.uploadprogrees) as? ProgressBar
val progressCard  = findViewById(R.id.progresscard) as? CardView
       val progressText  = findViewById(R.id.progresslable) as? TextView
// create alert dialog

// show it

       var metadata = StorageMetadata.Builder()
           .setContentType("audio/mp3")
           .build()
      val mReference = createStorageRefrence()

        try {
            mReference.putFile(uri,metadata).addOnSuccessListener {
                progressText!!.text = "100%"
                Toast.makeText(this, "Successfully Uploaded !!!!", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }.addOnPausedListener {

            }.addOnCanceledListener {

            }.addOnProgressListener {taskSnapshot->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
               progressCard!!.visibility = CardView.VISIBLE
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar!!.progress = progress.toInt()
                progressText!!.text = progress.toInt().toString()+"%";


                //Toast.makeText(this, progress.toInt().toString()+"%", Toast.LENGTH_LONG).show()
            }.addOnCompleteListener{
                progressText!!.text = "100%"
                val downloadlink = mReference.downloadUrl
                uploadInDatabase(downloadlink.toString())
                progressCard!!.visibility = CardView.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }

        }catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
    fun uploadInDatabase(
        link:String
    )
    {  val spinner  = findViewById(R.id.surahMenu) as Spinner
        val databs = FirebaseFirestore.getInstance()
        if (checkCode == 0)
        {
            val linkval = HashMap<String, Any>()
            linkval["link"] = link.toString()
            linkval["storageRef"] = downloadString.toString()


            databs.collection("parah").document(parahTf.selectedItem.toString())
                .set(linkval)


        }
        else if (checkCode == 1)
        {
            try {

                val linkval = HashMap<String, Any>()
                linkval["link"] = link.toString()
                linkval["storageRef"] = downloadString.toString()

                databs.collection("surah").document(spinner.selectedItem.toString() )
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
    fun createStorageRefrence(): StorageReference {
        val spinner  = findViewById(R.id.surahMenu) as Spinner
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        var mReference :StorageReference=storage.reference.child("parah")
        if(checkCode==0)
        { downloadString = "parah/"+parahTf.selectedItem.toString()
            mReference  = storage.reference.child("parah/"+parahTf.selectedItem.toString())

        }
       else if(checkCode==1)
        { downloadString = "Surah/"+spinner.selectedItem.toString()
            mReference  = storage.reference.child("surah/"+spinner.selectedItem.toString())
        }

        return mReference
    }



}
