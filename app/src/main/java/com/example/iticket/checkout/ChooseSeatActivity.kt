package com.example.iticket.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.iticket.R
import com.example.iticket.checkout.model.Checkout
import com.example.iticket.home.model.Movies
import kotlinx.android.synthetic.main.activity_choose_seat.*

class ChooseSeatActivity : AppCompatActivity() {

    var statusA3:Boolean = false
    var statusA4:Boolean = false
    var total:Int = 0

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat)

        val data = intent.getParcelableExtra<Movies>("data")

        tv_title.text = data.title

        a3.setOnClickListener {
            if (statusA3) {
                a3.setImageResource(R.drawable.ic_rectangle_empty)
                statusA3 = false
                total -=1
                buyTicket(total)

            } else {
                a3.setImageResource(R.drawable.ic_rectangle_selected)
                statusA3 = true
                total +=1
                buyTicket(total)

                val data = Checkout(
                    "A3",
                    "20000"
                )
                dataList.add(data)
            }
        }

        a4.setOnClickListener {
            if (statusA4) {
                a4.setImageResource(R.drawable.ic_rectangle_empty)
                statusA4 = false
                total -=1
                buyTicket(total)
            } else {
                a4.setImageResource(R.drawable.ic_rectangle_selected)
                statusA4 = true
                total +=1
                buyTicket(total)

                val data = Checkout(
                    "A4",
                    "20000"
                )
                dataList.add(data)
            }
        }

        btn_buy_ticket.setOnClickListener {

            val intent = Intent(
                this,
                CheckoutActivity::class.java
            ).putExtra("data", dataList)
            startActivity(intent)
        }

    }

    private fun buyTicket(total:Int) {
        if (total == 0) {
            btn_buy_ticket.setText("Buy Ticket")
            btn_buy_ticket.visibility = View.INVISIBLE
        } else if (total == 1){
            btn_buy_ticket.setText("Buy Ticket ("+total+")")
            btn_buy_ticket.visibility = View.VISIBLE
        } else {
            btn_buy_ticket.setText("Buy Tickets ("+total+")")
            btn_buy_ticket.visibility = View.VISIBLE
        }

    }
}