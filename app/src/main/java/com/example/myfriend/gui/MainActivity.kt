package com.example.myfriend.gui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.Menu
import android.view.MenuItem

import android.widget.*
import com.example.myfriend.Model.DBFriends

import android.widget.ListAdapter
import android.widget.ArrayAdapter
import com.example.myfriend.Model.BE_Friend
import com.example.myfriend.Model.FriendRepositoryInDB
import com.example.myfriend.R
import androidx.lifecycle.Observer



class MainActivity : AppCompatActivity() {

    val TAG = "xyz"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FriendRepositoryInDB.initialize(this)
        setupDataObserver()

        //insertTestData()
       /* val friends = DBFriends()

        val lV = findViewById<ListView>(R.id.lvFriends)

        val friendNames = friends.getAllNames()

        val ad: ListAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, friendNames
        )

        lV.adapter = ad


        lV.setOnItemClickListener { _, _, position, _ -> onListItemClick(position) }*/


    }

    private fun insertTestData() {
        val mRep = FriendRepositoryInDB.get()
        mRep.insert(BE_Friend(0,"Jonas","jonnystreed 21", "x1 y 20","25485652","Jonasgmail.com","jonweb.org", "02/05/1958","R.drawable.favorite"))
        mRep.insert(BE_Friend(0,"Timmy","Timmytreed 11", "x1 y 20","2123122","Timmygmail.com","Timmyweb.org", "02/01/1948","picuresting"))
        mRep.insert(BE_Friend(0,"BOB","BOBstreed 41", "x1 y 20","25235642","BOBgmail.com","BOBweb.org", "12/10/1998","picuresting"))
        mRep.insert(BE_Friend(0,"Sara","Sarastreed 21", "x1 y 20","24485611","Sarasgmail.com","Saraweb.org", "01/01/1918","picuresting"))
    }

    private fun setupDataObserver() {
        val mRep = FriendRepositoryInDB.get()
        val updateGUIObserver = Observer<List<BE_Friend>>{ friend ->
            val asStrings = friend.map { f -> "${f.id}, ${f.name}, ${f.address}, ${f.phoneNr}, ${f.picture}"}
            val adapter: ListAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                asStrings.toTypedArray()
            )
            val lV = findViewById<ListView>(R.id.lvFriends)
            lV.adapter = adapter
            Log.d(TAG, "UpdateGUI Observer notified")
        }
        mRep.getAllLiveData().observe(this, updateGUIObserver)

        val lV = findViewById<ListView>(R.id.lvFriends)
        lV.onItemClickListener = AdapterView.OnItemClickListener {_,_,pos,_ -> onListItemClick(pos)}
    }

    fun onListItemClick( position: Int ) {
        // position is in the list!
        // first get the name of the person clicked
        //val name = Friends().getAll()[position].name
        val intent = Intent(this, DetailsActivity::class.java)
        val friend = DBFriends().getAll()[position]
        intent.putExtra("friend", friend)
        startActivity(intent)

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

}




