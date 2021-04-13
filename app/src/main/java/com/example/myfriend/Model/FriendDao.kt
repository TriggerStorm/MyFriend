package com.example.myfriend.Model

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FriendDao {

    @Query("SELECT * from BE_Friend order by id")
    fun getAll(): LiveData<List<BE_Friend>>

    @Query("SELECT name from BE_Friend order by name")
    fun getAllNames(): LiveData<List<String>>

    @Query("SELECT * from BE_Friend where id = (:id)")
    fun getById(id: Int): LiveData<BE_Friend>

    @Insert
    fun insert(p: BE_Friend)

    @Update
    fun update(p: BE_Friend)

    @Delete
    fun delete(p: BE_Friend)

    @Query("DELETE from BE_Friend")
    fun deleteAll()
}