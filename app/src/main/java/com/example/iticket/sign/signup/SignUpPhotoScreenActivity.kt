package com.example.iticket.sign.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.iticket.R
import com.example.iticket.home.HomeActivity
import com.example.iticket.utils.Preferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath: Uri

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        tv_hello.text = "Welcome\n"+intent.getStringExtra("name")+"!"

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save_picture.visibility = View.INVISIBLE
                iv_add.setImageResource(R.drawable.btn_add_picture)
                iv_profile.setImageResource(R.drawable.user_pic)

            } else {
                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }
        }

        btn_upload_later.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@SignUpPhotoScreenActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        btn_save_picture.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoScreenActivity, "Uploaded", Toast.LENGTH_SHORT).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }

                        finishAffinity()
                        val intent = Intent(this@SignUpPhotoScreenActivity,
                            HomeActivity::class.java)
                        startActivity(intent)

                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoScreenActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            }

        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        //To change body of created functions use File | Settings | File Templates.
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "You cannot add your profile picture", Toast.LENGTH_LONG ).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "In a hurry? Upload profile photo later", Toast.LENGTH_LONG ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            statusAdd = true
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save_picture.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.btn_remove_picture)
        } else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
    }
}