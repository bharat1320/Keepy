package com.project.keepy.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.keepy.R
import com.project.keepy.databinding.ActivityMainBinding
import com.project.keepy.ui.login.LoginFragment
import com.project.keepy.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {

        fun LOG(data: String,activity: MainActivity) {
            Log.d("/@/", data)
            Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()
        }
        fun LOG(data: String) {
            Log.d("/@/", data)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        auth = Firebase.auth

        observer()

        if (mainViewModel.getAccountId(this) != null) {
            loadFragment(MainFragment())
        } else {
            loadFragment(LoginFragment())
        }

        startGoogleSignIn()
    }

    fun observer() {
        mainViewModel.callFragment.observe(this) {
            if (it != null) {
                loadFragment(it)
            }
        }

        mainViewModel.backPressed.observe(this) {
            this.onBackPressed()
        }
    }

    fun startGoogleSignIn() {
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        val singInIntent = googleSignInClient.signInIntent
        launcher.launch(singInIntent)
    }

    @SuppressLint("WrongConstant")
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            add(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }

        LOG("FragmentOpen :- ${fragment::class.simpleName.toString()}")
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container).toString()
        if (f.contains(MainFragment::class.simpleName.toString())) {
            this.finishAffinity()
        } else {
            super.onBackPressed()
        }

        Firebase.auth.signOut()
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful) {
            val account : GoogleSignInAccount? = task.result
            if(account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {

                        // On successful account login
                        LOG(account.email + " " + account.familyName,this)
                        val toast = Toast(this)
                        val view = ImageView(this)
                        Glide.with(this).load(account.photoUrl).into(view)
                        toast.setView(view)
                        toast.show()
                        loadFragment(MainFragment())



                    }else{
                        LOG(it.exception.toString())
                    }
                }
            }
        } else {
            LOG(task.exception.toString())
        }
    }
}