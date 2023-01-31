package com.project.keepy.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.keepy.R
import com.project.keepy.databinding.ActivityMainBinding
import com.project.keepy.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    companion object{
        fun LOG(data :String) {
            Log.d("/@/",data)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        observer()

        loadFragment(MainFragment.newInstance())

    }

    fun observer() {
        mainViewModel.callFragment.observe(this) {
            if(it != null) {
                loadFragment(it)
            }
        }

        mainViewModel.backPressed.observe(this) {
            this.onBackPressed()
        }
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

    // get current fragment displayed
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container).toString()
        if(f.contains(MainFragment::class.simpleName.toString())) {
            this.finishAffinity()
        } else {
            super.onBackPressed()
        }
    }
}