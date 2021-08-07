package com.demirli.a12flipimageexample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectedPicture: Uri? = null

    private var rotation: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rotation = 90f

        selectedImage_iv.setOnClickListener {
            checkPermission()
        }

        matrix_image_btn.setOnClickListener {
            image_1_iv.setImageURI(selectedPicture)
            image_2_iv.setImageURI(selectedPicture)
            image_3_iv.setImageURI(selectedPicture)
            image_4_iv.setImageURI(selectedPicture)
            matrixViewSetVisibilty(true)

        }

        cancel_matrix_btn.setOnClickListener {
            matrixViewSetVisibilty(false)
        }

        flip_image_btn.setOnClickListener {
            selectedImage_iv.rotation = rotation!!
            rotation = rotation!! + 90f
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionGranted(requestCode,permissions,grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        pictureSelected(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun checkPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }else{
            intentToGallery()
        }
    }

    fun permissionGranted(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                intentToGallery()
            }
        }
    }

    fun intentToGallery(){
        val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intentToGallery,Constants.INTENT_TO_GALLERY_REQUEST_CODE)
    }

    fun pictureSelected(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == Constants.INTENT_TO_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            selectedPicture = data.data
            selectedImage_iv.setImageURI(selectedPicture)
            matrix_image_btn.visibility = View.VISIBLE
        }
    }

    fun matrixViewSetVisibilty(isMatrixView: Boolean){
        if (isMatrixView == true){
            selectedImage_iv.visibility = View.INVISIBLE
            matrix_image_tl.visibility = View.VISIBLE
            cancel_matrix_btn.visibility = View.VISIBLE
            matrix_image_btn.visibility = View.INVISIBLE
            flip_image_btn.visibility = View.INVISIBLE
        }else if(isMatrixView == false){
            matrix_image_tl.visibility = View.INVISIBLE
            selectedImage_iv.visibility = View.VISIBLE
            matrix_image_btn.visibility = View.VISIBLE
            cancel_matrix_btn.visibility = View.INVISIBLE
            flip_image_btn.visibility = View.VISIBLE
        }
    }
}
