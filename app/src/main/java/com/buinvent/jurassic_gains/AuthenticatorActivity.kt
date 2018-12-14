package com.buinvent.jurassic_gains

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler
import com.amazonaws.mobile.auth.core.IdentityProvider
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration
import com.amazonaws.mobile.auth.ui.SignInActivity

class AuthenticatorActivity(private val service: AWSService) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticator)

        // Set up the callbacks to handle the authentication response
        service.identityManager.login(this, object : DefaultSignInResultHandler() {
            override fun onSuccess(activity: Activity, identityProvider: IdentityProvider) {
                Toast.makeText(this@AuthenticatorActivity,
                        String.format("Logged in as %s", service.identityManager.getCachedUserID()),
                        Toast.LENGTH_LONG).show()
                // Go to the main activity
                val intent = Intent(activity, WeekActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activity.startActivity(intent)
                activity.finish()
            }

            override fun onCancel(activity: Activity): Boolean {
                return false
            }
        })

        // Start the authentication UI
        val config = AuthUIConfiguration.Builder()
                .userPools(true)
                .build()
        SignInActivity.startSignInActivity(this, config)
        this@AuthenticatorActivity.finish()
    }
}
