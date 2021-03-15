package com.example.myfriend.Model

class DBFriends {
    val friendsList = arrayOf<BE_Friend>(
        BE_Friend("Jonas","25485652",true),
        BE_Friend("jim", "21020101",false),
        BE_Friend("Nico", "22110101",true),
        BE_Friend("Kimmi","20100501",false)
    )

    fun getAll(): Array<BE_Friend> = friendsList

    fun getAllNames(): Array<String> = friendsList.map { f-> f.name }.toTypedArray()

}