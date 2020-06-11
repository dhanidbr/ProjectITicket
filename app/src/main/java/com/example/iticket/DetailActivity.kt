package com.example.iticket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.example.iticket.checkout.ChooseSeatActivity
import com.example.iticket.home.dashboard.PlaysAdapter
import com.example.iticket.home.model.Movies
import com.example.iticket.home.model.Plays
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data = intent.getParcelableExtra<Movies>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Movies")
            .child(data.title.toString())
            .child("actor")

        tv_title.text = data.title
        tv_genre.text = data.genre
        tv_desc.text = data.desc
        tv_rate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster)

        btn_choose_seat.setOnClickListener {
            val intent = Intent(this@DetailActivity,
                ChooseSeatActivity::class.java).putExtra("data", data)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

        rv_who_play.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val movies = getdataSnapshot.getValue(Plays::class.java!!)
                    dataList.add(movies!!)
                }

                rv_who_play.adapter = PlaysAdapter(dataList) {

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}