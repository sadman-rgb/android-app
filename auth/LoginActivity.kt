package com.example.cloudstorage.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudstorage.ui.MainActivity
import com.example.stoicidentity.StoicIdentity // Library Stoic Identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginWithInternetIdentity()
    }

    private fun loginWithInternetIdentity() {
        GlobalScope.launch(Dispatchers.Main) {
            val identity = StoicIdentity.connect(this@LoginActivity)
            if (identity != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                // Handle login failure
            }
        }
    }
}
