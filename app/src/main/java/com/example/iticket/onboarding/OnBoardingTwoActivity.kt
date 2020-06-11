package com.example.iticket.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.R
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class OnBoardingTwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_two)

        btn_next.setOnClickListener {
            val intent = Intent(this@OnBoardingTwoActivity,
                OnBoardingThreeActivity::class.java)
            startActivity(intent)
        }
    }
}