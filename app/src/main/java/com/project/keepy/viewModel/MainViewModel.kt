package com.project.keepy.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.keepy.ui.MainActivity

class MainViewModel : ViewModel() {

    val callFragment : MutableLiveData<Fragment> = MutableLiveData()

    val backPressed : MutableLiveData<Boolean> = MutableLiveData()

    var sharedPreferences : SharedPreferences? = null

    fun callFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = Bundle()
        callFragment.postValue(fragment)
    }

    fun backPressed() {
        backPressed.postValue(true)
    }

    private fun getSharedPreferenceOfAccount(activity: MainActivity) : SharedPreferences?{
        return if(sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences("Account", Context.MODE_PRIVATE)
            return sharedPreferences
        } else sharedPreferences
    }

    fun getAccountId(activity: MainActivity) : String? {
        val account = getSharedPreferenceOfAccount(activity)
        return if(account?.contains("accountId") == true) {
            account.getString("accountId",null)
        } else null
    }


}