package com.jogdand.roomtest

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * @author Rushikesh Jogdand.
 */
@Dao
interface DataAo {
    @Insert
    fun addData(data: Data): Long

    @Query("SELECT * FROM Data")
    fun fetchData(): LiveData<MutableList<Data>>
}
