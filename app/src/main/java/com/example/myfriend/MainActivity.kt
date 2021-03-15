package com.example.myfriend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    val TAG = "xyz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txt1 = findViewById<TextView>(R.id.txt1)

        registerForContextMenu(txt1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()

        when (id) {
            R.id.action_settings -> {
                Toast.makeText(this, "Action Settings selected...", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_new -> {
                val intent = Intent(this, DetailsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_close -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        Log.d(TAG, "Context menu created ")
        if (v === findViewById<TextView>(R.id.txt1)) {
            menuInflater.inflate(R.menu.context_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "Context item selected " + item.itemId)
        return when (item.itemId) {
            R.id.create1 -> {
                Toast.makeText(baseContext, "CREATE", Toast.LENGTH_LONG).show()
                true
            }
            R.id.update1 -> {
                    val intent = Intent(this, DetailsActivity::class.java)
                    startActivity(intent)
                true
            }
            R.id.print -> {
                Toast.makeText(this, "Print ....", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}




