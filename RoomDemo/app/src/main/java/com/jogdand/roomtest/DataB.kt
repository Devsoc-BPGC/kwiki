package com.jogdand.roomtest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * @author Rushikesh Jogdand.
 */

var INSTANCE: DataB? = null
const val DB_NAME = "roomTestDb"
fun getDatabase(context: Context): DataB? {
    if (INSTANCE == null) {
        synchronized(DataB::class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,
                        DataB::class.java,
                        DB_NAME).build()
            }
        }
    }
    return INSTANCE
}

@Database(entities = [Data::class], version = 1)
abstract class DataB : RoomDatabase() {
    abstract fun dao(): DataAo

}
