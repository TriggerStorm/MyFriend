package com.example.myfriend.Model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BE_Friend (
        @PrimaryKey(autoGenerate = true)
                 var id:Int,
                 var name: String,
                 var address: String,
                 var location: String,
                 var phoneNr: String,
                 var mail: String,
                 var webside: String,
                 var birthday: String,
                 var picture: String
                 ) : Serializable {


}