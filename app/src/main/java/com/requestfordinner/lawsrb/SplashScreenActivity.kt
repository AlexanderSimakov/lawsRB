package com.requestfordinner.lawsrb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/** This activity will be shown while the [MainActivity] is loading. */
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }

        startActivity(intent)
        finish()
    }
}