package io.github.sainak.trackmysleepquality.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.github.sainak.trackmysleepquality.R
import io.github.sainak.trackmysleepquality.databinding.ActivityMainBinding
import io.github.sainak.trackmysleepquality.util.addSystemWindowInsetToMargin
import io.github.sainak.trackmysleepquality.util.addSystemWindowInsetToPadding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        // Set view flags so that we can tell the system to draw behind the navBar and statusBar
        @Suppress("DEPRECATION")// I know but we still have to deal with it
        binding.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        hideFab()

        // add insets to views
        binding.appbar.addSystemWindowInsetToPadding(top = true)
        binding.fabTracker.addSystemWindowInsetToMargin(bottom = true)
    }

    fun showFabWithListener(function: () -> Unit, imgSrcId: Int) {
        binding.fabTracker.apply {
            setOnClickListener { function() }
            setImageResource(imgSrcId)
            show()
        }
    }

    fun hideFab() {
        binding.fabTracker.apply {
            setOnClickListener(null) // should remove all attached listeners
            hide()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

}