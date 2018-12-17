package com.buinvent.jurassic_gains

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

import android.widget.Toast



class AuthActivity : Activity() {

    private val RC_SIGN_IN = 123

    val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.jlogo)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null && user.displayName != null)
                    Toast.makeText(this@AuthActivity, "Logged in as " + user.displayName,
                            Toast.LENGTH_SHORT).show()

                startActivity(Intent(applicationContext, WeekActivity::class.java))

            } else {
            }
        }
    }


}
