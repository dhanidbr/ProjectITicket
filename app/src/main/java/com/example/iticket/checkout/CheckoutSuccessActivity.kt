package com.example.iticket.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.R
import com.example.iticket.home.HomeActivity
import kotlinx.android.synthetic.main.activity_checkout_success.*

class CheckoutSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        btn_back.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@CheckoutSuccessActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

    }
}