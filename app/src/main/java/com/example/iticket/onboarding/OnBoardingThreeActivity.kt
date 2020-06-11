package com.example.iticket.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.R
import com.example.iticket.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_on_boarding_three.*

class OnBoardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_three)

        btn_getting_started.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@OnBoardingThreeActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }

}