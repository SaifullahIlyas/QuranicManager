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
import com.squareup.picasso.Picasso


class Register : AppCompatActivity() {
    private val GALLERY = 1
    private val CAMERA = 2
    private   var FLAG = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val userImage = findViewById<ImageView>(R.id.userimage)//select image from the phone for user
        userImage.setOnClickListener {
            showPictureDialog()


        }
        val registerBtn = findViewById<Button>(R.id.registerBtn)
            registerBtn.setOnClickListener {
                val view = it;
                view.isEnabled = false
                var StorageRef = ""
                val profilename = findViewById<EditText>(R.id.profilename)
                var email: String = "Default";
                val auth = FirebaseAuth.getInstance();
                if (auth != null) {
                    email = auth.currentUser?.email!!.decapitalize();
                }
                val fs = FirebaseStorage.getInstance()
                var metadata = StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build()
                val ffs = FirebaseFirestore.getInstance()
                val refrence = fs.getReference("users").child(email)
                userImage.isDrawingCacheEnabled = true
                userImage.buildDrawingCache()
                if (FLAG == true) {
                    val bitmap = (userImage?.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()







                    if (!profilename.text.isEmpty()) {
                        val path = refrence.putBytes(data, metadata).addOnSuccessListener {
                            val data = HashMap<String, Any>()
                            data["email"] = email
                            data["profilename"] = profilename.text.toString();
                            data["storageref"] = "users/" + email.toString()


                            ffs.collection("users").document(email).set(data).addOnSuccessListener {
                                Toast.makeText(this, "Suuccesss profile updated!!!", Toast.LENGTH_LONG).show()
                                profilename.setText("")
                                view.isEnabled = true
                                FLAG = false

                            }.addOnFailureListener {
                                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                                view.isEnabled = true
                                FLAG = false
                            }


                        }.addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                            view.isEnabled = true
                            FLAG = false
                        }

                    } else {
                        Toast.makeText(this, "Enter profile name first", Toast.LENGTH_LONG).show()
                        view.isEnabled = true
                    }
                }
                else
                {
                    if(!profilename.text.isEmpty())
                    {
                        val data = HashMap<String, Any>()
                        data["email"] = email
                        data["profilename"] = profilename.text.toString();
                        ffs.collection("users").document(email).set(data).addOnSuccessListener {
                            Toast.makeText(this, "Suuccesss profile updated!!!", Toast.LENGTH_LONG).show()
                            profilename.setText("")
                            view.isEnabled = true

                        }.addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                            view.isEnabled = true
                        }
                    }
                    else
                        Toast.makeText(this,"Profile name required",Toast.LENGTH_LONG).show()
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
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, GALLERY)
    }


    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
           Picasso.get().load(data?.data).resize(userimage.width,userimage.height).into(userimage)
            FLAG = true
        }
        else if(resultCode==Activity.RESULT_OK&& requestCode==CAMERA)
        {
           // userimage.setImageURI(data?.data)
           // val imageView = findViewById(R.id.ImageViewId) as ImageView
            val imageBitmap = data?.extras?.get("data") as Bitmap
            userimage.setImageBitmap(imageBitmap)
            FLAG = true
        }

    }
}
