package com.jagito.findTheWordKotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jagito.findTheWordKotlin.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btn_start.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        btn_about.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        btn_exit.setOnClickListener { finishAffinity() }
    }
}
