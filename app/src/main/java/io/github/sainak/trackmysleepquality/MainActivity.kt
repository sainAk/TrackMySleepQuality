package io.github.sainak.trackmysleepquality

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.github.sainak.trackmysleepquality.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        //handle item clicks
        if (id == R.id.action_settings) {
            //do your action here, im just showing toast
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

}