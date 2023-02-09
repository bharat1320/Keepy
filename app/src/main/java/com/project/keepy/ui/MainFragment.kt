package com.project.keepy.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.keepy.R
import com.project.keepy.databinding.ActivityMainBinding
import com.project.keepy.databinding.FragmentMainBinding
import com.project.keepy.viewModel.MainViewModel

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

//    companion object {
//        fun newInstance() : MainFragment = this.newInstance()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


    }
}