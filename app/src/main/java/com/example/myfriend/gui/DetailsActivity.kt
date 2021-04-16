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
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.example.myfriend.Model.BE_Friend
import com.example.myfriend.Model.FriendRepositoryInDB
import com.example.myfriend.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class DetailsActivity : AppCompatActivity() {
    var areNew: Boolean = false

    var PHONE_NO = "12345678"

    val TAG = "xyz"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //this part is defining if is a new or existing friend
        var isNew = getIntent().getExtras()?.getBoolean("isNew");
        if (isNew != null) {
            areNew = isNew
        }
        // remove log.
        Log.d("xyz", isNew.toString())
        Log.d("xyz", areNew.toString())

        // if is a known friend it will run this code for edit the Friend profile
        if(isNew === false) {

            // finding the view for reface from the Activity
           val tvName = findViewById<EditText>(R.id.ip_Name)
            val tvPhone = findViewById<EditText>(R.id.ip_phone)
            val address = findViewById<EditText>(R.id.ip_Address)
            val mail = findViewById<EditText>(R.id.ip_Email)
            val webside =findViewById<EditText>(R.id.ip_Website)
            val birthday = findViewById<EditText>(R.id.ip_Bday)
            val img = findViewById<ImageView>(R.id.img_pic)


            /*
            val home = findViewById<Button>(R.id.bn_Home)
            val show = findViewById<Button>(R.id.bn_Show)
            val pcall = findViewById<ImageButton>(R.id.ibn_call)
            val pdel = findViewById<ImageButton>(R.id.ibn_delete)
            val ptext = findViewById<ImageButton>(R.id.ibn_text)
            val save = findViewById<Button>(R.id.bn_Save)
            */

            // if the extras is not null it will get the info about the Be_friend form the friend click in main activity
            if (intent.extras != null) {
                val extras: Bundle = intent.extras!!
                val friend = extras.getSerializable("friend") as BE_Friend


                val res: Resources = resources
                val mDrawableName = friend.picture
                val resID: Int = res.getIdentifier(mDrawableName, "drawable", packageName)


                //defining events and value in the view items with onclick and setting the value of the BE.
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
                // if is a new Friend hide un use / necessary buttons and img.
                img.visibility = View.INVISIBLE
                /*home.isVisible = false
                show.visibility = View.INVISIBLE
                pcall.visibility = View.INVISIBLE
                pdel.visibility = View.INVISIBLE
                ptext.visibility = View.INVISIBLE*/




            }
        }
        else{

        }
    }

        val REQUEST_IMAGE_CAPTURE = 1

    fun addNewFriend(view: View) {

        // rough way of getting and setting defriend view elements and getting there value as string.   Note..... plz remind me what the import was to get direct access to the fx ip_Name form the xml THX!!
        val mRep = FriendRepositoryInDB.get()
        val tvName = findViewById<EditText>(R.id.ip_Name)
        val tvPhone = findViewById<EditText>(R.id.ip_phone)
        val mail = findViewById<EditText>(R.id.ip_Email)
        val address = findViewById<EditText>(R.id.ip_Address)
        val webside =findViewById<EditText>(R.id.ip_Website)
        val birthday = findViewById<EditText>(R.id.ip_Bday)
        val img = findViewById<ImageView>(R.id.img_pic)
        var mailtext = mail.text.toString()
        var nametext = tvName.text.toString()
        var adressText = address.text.toString()
        var nrtext = tvPhone.text.toString()
        var webText = webside.text.toString()
        var bdText = birthday.text.toString()
        var imgText = ""


        // adding new Friend to DB or else updating existing friend
        if(areNew == true){
        mRep.insert(BE_Friend(0,nametext,adressText, "",nrtext,mailtext,webText, bdText,""))
        }else
            mRep.update((BE_Friend(0,nametext,adressText, "",nrtext,mailtext,webText, bdText,imgText)))

    }

         private fun dispatchTakePictureIntent() {
             // seting the intent for the img capture
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
             // requesting to do img capture or catch eros
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }


        }
    //if the request of the img capture and the result is oka then its mapping the data form the capture in a bit map and setting it as img. in view.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            val img = findViewById<ImageView>(R.id.img_pic)
            img.setImageBitmap(imageBitmap)
        }

    }

    lateinit var currentPhotoPath: String

    // creating img file name and getting the external storage and creating the file for storage
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



    // seting intent for useing call funktion on the phone and setting the number before executing the intent.
    fun onClickCALL(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$PHONE_NO")
        startActivity(intent)
    }

    //setting intent for useing email funktion on the phone and setting reciver before executing on the intent to sent mails.
    fun onClickEMAIL(view: View) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        val mail = findViewById<EditText>(R.id.ip_Email)
        val receivers = arrayOf(mail.text.toString())
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers)
        startActivity(emailIntent)
    }
    // seting intent for using phone web servie and setting url for webside berore calling the intent.
    fun onClickBROWSER(view: View) {
        val i = Intent(Intent.ACTION_VIEW)
        val webside = findViewById<EditText>(R.id.ip_Website)
        val url = "http://" + webside.text.toString()
        i.data = Uri.parse(url)
        startActivity(i)
    }
    // seting intent for using phone sms app before setting reseaver and calling the intent for sms the intented nr.
    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:$PHONE_NO")
        startActivity(sendIntent)
    }


    // checking on Permission before calling funktion send message
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }

    // setting up smsmanger and prep defuld start text for sms app
    private fun sendMessage() {
        val m = SmsManager.getDefault()
        val text = ""
        m.sendTextMessage(PHONE_NO, null, text, null, null)
    }
    // call function showYesNoDialog
    fun onClickSMS(view: View) {
        showYesNoDialog()
    }
    // setting up alert for the user before routing to the sms app
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
        // call funktion to start lisen for gps cordinates
        startListening()
        // setiing up location service and gps provider
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val slocaton = location.toString()

        // if extras is not null finde be of selcted friend and add new location to db.
        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val friend = extras.getSerializable("friend") as BE_Friend

        Toast.makeText(this, slocaton, Toast.LENGTH_LONG).show()
        Log.d("tag", slocaton)
        friend.location = location.toString()

        val mRep = FriendRepositoryInDB.get()
        // updating new loaction
       if (location != null) {
            mRep.update(friend)
        }
            else{
            return}
        }
    }

    var myLocationListener: LocationListener? = null

    @SuppressLint("MissingPermission")
    private fun startListening() {
        // creating a new location listner if its null and start counting for new chance of location.
        if (myLocationListener == null)
            myLocationListener = object : LocationListener {
                var count: Int = 0

                override fun onLocationChanged(location: Location) {
                    count++
                    Log.d(TAG, "Location changed")
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }
            }
        // getting location service and gps and updating the latest new location from mylocationlistener
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0.0F,
                myLocationListener!!)

    }
    // stops the loping lisen on the gps
    private fun stopListening() {

        if (myLocationListener == null) return

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(myLocationListener!!)
    }
    /* stops Listening on gps when close the activity */
    override fun onStop(){
        stopListening()
        super.onStop()
    }
    // deleting selected Friend form the DB.
    fun onClicKDelete(view: View) {
        val mRep = FriendRepositoryInDB.get()
        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val friend = extras.getSerializable("friend") as BE_Friend

            mRep.delete(friend)
            finish()
        }
            else
        {
            return
        }
    }


}