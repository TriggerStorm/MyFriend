package com.example.myfriend.gui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myfriend.Model.BE_Friend
import com.example.myfriend.Model.FriendRepositoryInDB
import com.example.myfriend.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetailsActivity : AppCompatActivity() {
    val isNew: Boolean = false

    var PHONE_NO = "12345678"
    val TAG = "xyz"

    var dfriend = "" as BE_Friend

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
            if (intent.extras != null) {
                val extras: Bundle = intent.extras!!
                val friend = extras.getSerializable("friend") as BE_Friend

                dfriend = friend
                val res: Resources = resources
                val mDrawableName = friend.picture
                val resID: Int = res.getIdentifier(mDrawableName, "drawable", packageName)


                img.setOnClickListener { dispatchTakePictureIntent() }

                PHONE_NO = friend.phoneNr

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

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
   /* private fun dispatchTakePictureIntent2() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }*/


    fun onClickSMS(view: View) {
        showYesNoDialog()
    }

    fun onClickCALL(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$PHONE_NO")
        startActivity(intent)
    }

    fun onClickEMAIL(view: View) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        val mail = findViewById<EditText>(R.id.ip_Email)
        val receivers = arrayOf(mail.text.toString())
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers)
        startActivity(emailIntent)
    }

    fun onClickBROWSER(view: View) {
        val i = Intent(Intent.ACTION_VIEW)
        val webside = findViewById<EditText>(R.id.ip_Website)
        val url = "http://" + webside.text.toString()
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:$PHONE_NO")
        startActivity(sendIntent)
    }

   //val PERMISSION_REQUEST_CODE = 1



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val m = SmsManager.getDefault()
        val text = ""
        m.sendTextMessage(PHONE_NO, null, text, null, null)
    }
    private fun showYesNoDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("SMS Handling")
        alertDialogBuilder
                .setMessage("Click Start to start SMS app...")
                .setCancelable(true)
                .setNegativeButton("Start", { dialog, id -> startSMSActivity() })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    @SuppressLint("MissingPermission")
    fun onClickGetLocation(view: View) {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        dfriend.location = location.toString()

        val mRep = FriendRepositoryInDB.get()

        if (dfriend !== "" as BE_Friend) {
            mRep.update(dfriend)
        }
    }


}