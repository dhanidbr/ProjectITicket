package com.example.iticket.sign.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.sign.signin.User
import com.example.iticket.utils.Preferences
import com.google.firebase.database.*
import com.example.iticket.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var sUsername:String
    lateinit var sPassword:String
    lateinit var sName:String
    lateinit var sEmail:String

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        preferences = Preferences(this)

        btn_sign_up.setOnClickListener {
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sName = et_name.text.toString()
            sEmail = et_email.text.toString()

            if (sUsername == "") {
                et_username.error = "Please fill your password"
                et_username.requestFocus()
            } else if (sPassword == "") {
                et_password.error = "Please fill your password"
                et_password.requestFocus()
            } else if (sName == "") {
                et_name.error = "Please fill your name"
                et_name.requestFocus()
            } else if (sEmail == "") {
                et_email.error = "Please fill your email"
                et_email.requestFocus()
            } else {
                saveUser(sUsername, sPassword, sName, sEmail)
            }
        }
    }

    private fun saveUser(sUsername: String, sPassword: String, sName: String, sEmail: String) {

        val user = User()
        user.email = sEmail
        user.username = sUsername
        user.name = sName
        user.password = sPassword

        if (sUsername != null) {
            checkingUsername(sUsername, user)
        }

    }

    private fun checkingUsername(iUsername: String, data: User) {
        mFirebaseDatabase.child(iUsername).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    mFirebaseDatabase.child(iUsername).setValue(data)

                    preferences.setValues("name", data.name.toString())
                    preferences.setValues("user", data.username.toString())
                    preferences.setValues("url", "")
                    preferences.setValues("email", data.email.toString())
                    preferences.setValues("balance","200000")
                    preferences.setValues("status", "1")

                    val intent = Intent(this@SignUpActivity,
                        SignUpPhotoScreenActivity::class.java).putExtra("name", data.name)
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUpActivity, "User is already taken", Toast.LENGTH_LONG).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}