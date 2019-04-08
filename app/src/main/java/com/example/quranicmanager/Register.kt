package com.example.quranicmanager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection

import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata


class Register : AppCompatActivity() {
    private val GALLERY = 1
    private val CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val userImage = findViewById<ImageView>(R.id.userimage)//select image from the phone for user
        userImage.setOnClickListener {
            Toast.makeText(this, "Image is Pressed", Toast.LENGTH_SHORT).show()
            showPictureDialog()


        }
        val registerBtn = findViewById<Button>(R.id.registerBtn)
            registerBtn.setOnClickListener {
                val profilename = findViewById<EditText>(R.id.profilename)
                var email: String = "Default";
                val auth = FirebaseAuth.getInstance();
                if (auth != null) {
                    email = auth.currentUser?.email!!;
                }
                val fs = FirebaseStorage.getInstance()
                var metadata = StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build()
                userImage.isDrawingCacheEnabled = true
                userImage.buildDrawingCache()
                val bitmap = (userImage?.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val ffs = FirebaseFirestore.getInstance()
          val refrence   =  fs.getReference("users").child(email)


                if (!profilename.text.isEmpty()) {
               val path   =  refrence.putBytes(data, metadata).addOnSuccessListener {
                        val data = HashMap<String, Any>()
                        data["email"] = email
                        data["profilename"] = profilename.text.toString();
                        data["storageref"] = fs.reference.toString()

                        ffs.collection("users").document(email).set(data).addOnSuccessListener {
                            Toast.makeText(this, "Suuccesss!!!", Toast.LENGTH_LONG).show()
                            profilename.setText("")
                        }.addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }


                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                }
                else{
                    Toast.makeText(this,"Enter profile name first",Toast.LENGTH_LONG).show()
                }
            }


    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Image Form")
        val pictureDialogItems = arrayOf("Gallery", "Camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }


    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            userimage.setImageBitmap(imageBitmap!!)

        }
        else if(resultCode==Activity.RESULT_OK&& requestCode==CAMERA)
        {
           // userimage.setImageURI(data?.data)
           // val imageView = findViewById(R.id.ImageViewId) as ImageView
            val imageBitmap = data?.extras?.get("data") as Bitmap
            userimage.setImageBitmap(imageBitmap)
        }

    }
}
