package com.example.myfriend.Model

class DBFriends {
    val friendsList = arrayOf<BE_Friend>(
        BE_Friend(1,"Jonas","jonnystreed 21", "x1 y 20","25485652","Jonasgmail.com","jonweb.org", "02/05/1958","R.drawable.favorite"),
        BE_Friend(2,"Timmy","Timmytreed 11", "x1 y 20","2123122","Timmygmail.com","Timmyweb.org", "02/01/1948","picuresting"),
        BE_Friend(3,"BOB","BOBstreed 41", "x1 y 20","25235642","BOBgmail.com","BOBweb.org", "12/10/1998","picuresting"),
        BE_Friend(4,"Sara","Sarastreed 21", "x1 y 20","24485611","Sarasgmail.com","Saraweb.org", "01/01/1918","picuresting")
    )

    fun getAll(): Array<BE_Friend> = friendsList

    fun getAllNames(): Array<String> = friendsList.map { f-> f.name }.toTypedArray()

}