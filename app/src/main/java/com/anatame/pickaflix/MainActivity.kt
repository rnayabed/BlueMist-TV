package com.anatame.pickaflix

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.anatame.pickaflix.common.utils.HeadlessWebViewHelper
import com.anatame.pickaflix.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headlessWebViewHelper : HeadlessWebViewHelper
    private lateinit var headlessWebViewHelperInstance: HeadlessWebViewHelper.Instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_saved, R.id.navigation_downloads
            )
        )

        val iconColorStates = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                ContextCompat.getColor(this, R.color.UnselectedIconColor),
                ContextCompat.getColor(this, R.color.Primary)
            )
        )

        navView.setItemIconTintList(iconColorStates)
        navView.setItemTextColor(iconColorStates)

        navView.setupWithNavController(navController)

        headlessWebViewHelper = HeadlessWebViewHelper(binding.hWebView, this)
        headlessWebViewHelperInstance = headlessWebViewHelper.initView()

        val shrd = getSharedPreferences("settings", Context.MODE_PRIVATE)
        Log.d("isFirstLoad", shrd.getBoolean("isFirstLoad", false).toString())
        if(shrd.getBoolean("isFirstLoad", false) ){

        } else {

            headlessWebViewHelperInstance.loadUrl("https://rabbitstream.net/embed-4/2OSbWoyw5rBE?z=")
            headlessWebViewHelperInstance.setOnStreamUrlLoadedListener {
                Log.d("loadingFromMainActivity", "well")
                val editor = shrd.edit()
                editor.apply{
                    putBoolean("isFirstLoad", true)
                }.apply()
            }
        }
    }


    fun getWebPlayer(): HeadlessWebViewHelper.Instance {
        return headlessWebViewHelperInstance
    }

    fun showWebView() {
        if(binding.hWebView.visibility == View.VISIBLE){
            binding.hWebView.visibility = View.INVISIBLE
        } else {
            binding.hWebView.visibility = View.VISIBLE
        }
    }

    fun showBottomNav(){
        if(this::binding.isInitialized) {
            binding.navView.visibility = View.VISIBLE
        }
    }

    fun hideBottomNav(){
        if(this::binding.isInitialized) {
            binding.navView.visibility = View.GONE
        }
    }
}