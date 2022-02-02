package com.anatame.pickaflix

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
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

        navView.setupWithNavController(navController)

        headlessWebViewHelper = HeadlessWebViewHelper(binding.hWebView, this)
        headlessWebViewHelperInstance = headlessWebViewHelper.initView()
        headlessWebViewHelperInstance.loadUrl("https://fmovies.to/movie/eternals-5wwrq/1-full", false)
    }

    fun getWebPlayer(): HeadlessWebViewHelper.Instance {
        return headlessWebViewHelperInstance
    }
}