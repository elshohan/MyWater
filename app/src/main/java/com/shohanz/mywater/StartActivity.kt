package com.shohanz.mywater

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    override fun onStart() {
        super.onStart()
        getStarted.setOnClickListener {
            startActivity(Intent(this, StartUserActivity::class.java))
            finish()
        }
    }
}
