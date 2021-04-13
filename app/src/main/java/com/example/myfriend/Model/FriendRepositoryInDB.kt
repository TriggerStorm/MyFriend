package com.example.myfriend.Model



import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class FriendRepositoryInDB  {

    val TAG = "xyz"

    private val database: FriendDatabase

    private val friendDao : FriendDao

    private lateinit var cache: List<BE_Friend>

    private constructor(context: Context) {

        database = Room.databaseBuilder(context.applicationContext,
            FriendDatabase::class.java,
            "friend-database").build()

        friendDao = database.friendDao()

        val updateCacheObserver = Observer<List<BE_Friend>>{ persons ->
            cache = persons;
            Log.d(TAG, "Update Cache observer notified")
        }
        getAllLiveData().observe(context as LifecycleOwner, updateCacheObserver)
    }

    fun getAllLiveData(): LiveData<List<BE_Friend>> = friendDao.getAll()


    fun getById(id: Int): BE_Friend? {
        cache.forEach { p -> if (p.id == id) return p; }
        return null;
    }

    fun getByPos(pos: Int): BE_Friend? {
        if (pos < cache.size)
            return cache[pos]
        return null
    }


    private val executor = Executors.newSingleThreadExecutor()

    fun insert(p: BE_Friend) {
        executor.execute{ friendDao.insert(p) }
    }

    fun update(p: BE_Friend) {
        executor.execute { friendDao.update(p) }
    }

    fun delete(p: BE_Friend) {
        executor.execute { friendDao.delete(p) }
    }

    fun clear() {
        executor.execute { friendDao.deleteAll() }
    }


    companion object {
        private var Instance: FriendRepositoryInDB? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = FriendRepositoryInDB(context)
        }

        fun get(): FriendRepositoryInDB {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Person repo not initialized")
        }
    }







}