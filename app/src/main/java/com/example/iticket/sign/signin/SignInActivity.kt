package com.example.iticket.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.home.HomeActivity
import com.example.iticket.sign.signup.SignUpActivity
import com.example.iticket.utils.Preferences
import com.google.firebase.database.*
import com.example.iticket.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    lateinit var iUsername: String
    lateinit var iPassword: String
    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(this@SignInActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        btn_sign_in.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if (iUsername == "") {
                et_username.error = "Please type your username"
                et_username.requestFocus()
            } else if (iPassword == "") {
                et_password.error = "Please type your password"
                et_password.requestFocus()
            } else {

                var statusUsername = iUsername.indexOf(".")
                if (statusUsername >= 0) {
                    et_username.error = "Please type your password without ."
                    et_username.requestFocus()
                } else {
                    pushLogin(iUsername, iPassword)
                }
            }
        }

        btn_create_account.setOnClickListener {
            val intent = Intent(this@SignInActivity,
                SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User not found", Toast.LENGTH_LONG).show()

                } else {
                    if (user.password.equals(iPassword)){
                        Toast.makeText(this@SignInActivity, "Welcome!", Toast.LENGTH_LONG).show()

                        preferences.setValues("name", user.name.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("balance", user.balance.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(this@SignInActivity,
                            HomeActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@SignInActivity, "Wrong password!", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}