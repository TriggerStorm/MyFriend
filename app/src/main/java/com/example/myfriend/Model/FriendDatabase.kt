package com.example.myfriend.Model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BE_Friend::class], version=1)
abstract class FriendDatabase  : RoomDatabase() {

    abstract fun friendDao(): FriendDao
}