package com.example.nikestore.feature.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikestore.R
import com.example.nikestore.common.NikeActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,LoginFragment())
        }.commit()

    }
}