package com.example.iticket.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.R
import com.example.iticket.sign.signin.SignInActivity
import com.example.iticket.utils.Preferences
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class OnBoardingOneActivity : AppCompatActivity() {

    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()

            val intent = Intent(this@OnBoardingOneActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }


        btn_next.setOnClickListener {
            val intent = Intent(
                this@OnBoardingOneActivity,
                OnBoardingTwoActivity::class.java
            )
            startActivity(intent)
        }

        btn_skip.setOnClickListener {
            finishAffinity()

            val intent = Intent(
                this@OnBoardingOneActivity,
                SignInActivity::class.java
            )
            startActivity(intent)
        }
    }
}