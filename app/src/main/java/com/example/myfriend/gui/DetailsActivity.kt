package com.example.myfriend.gui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myfriend.Model.BE_Friend
import com.example.myfriend.R


class DetailsActivity : AppCompatActivity() {
    val isNew: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        if(isNew === false) {
           val tvName = findViewById<EditText>(R.id.ip_Name)
            val tvPhone = findViewById<EditText>(R.id.ip_phone)
            val address = findViewById<EditText>(R.id.ip_Address)
            val mail = findViewById<EditText>(R.id.ip_Email)
            val webside =findViewById<EditText>(R.id.ip_Website)
            val birthday = findViewById<EditText>(R.id.ip_Bday)
            val img = findViewById<ImageView>(R.id.img_pic)
            //val savebn = findViewById<Button>()
            //val picture = findViewById<>(R.id.imgPicture)
            if (intent.extras != null) {
                val extras: Bundle = intent.extras!!
                val friend = extras.getSerializable("friend") as BE_Friend

                val res: Resources = resources
                val mDrawableName = friend.picture
                val resID: Int = res.getIdentifier(mDrawableName, "drawable", packageName)
               // val drawable: Drawable = res.getDrawable(resID)

                img.setOnClickListener { dispatchTakePictureIntent() }


                img.setImageResource(R.drawable.trash)
                birthday.setText(friend.birthday)
                webside.setText(friend.webside)
                mail.setText(friend.mail)
                address.setText(friend.address)
                tvName.setText(friend.name)
                tvPhone.setText(friend.phoneNr)
            } else {
                Log.d("xyz", "system error: intent.extras for detailactivity is null!!!!")
            }
        }
        else{

        }
    }
        val REQUEST_IMAGE_CAPTURE = 1

         private fun dispatchTakePictureIntent() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }


        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            val img = findViewById<ImageView>(R.id.img_pic)
            img.setImageBitmap(imageBitmap)
        }

    }


}