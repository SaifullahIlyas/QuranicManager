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
            Toast.makeText(this, "here is the user register code", Toast.LENGTH_SHORT).show()
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
            userimage.setImageURI(data?.data)
        }

    }
}
