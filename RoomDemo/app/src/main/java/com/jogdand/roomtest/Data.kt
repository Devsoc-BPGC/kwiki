package com.jogdand.roomtest

import androidx.room.Entity

/**
 * @author Rushikesh Jogdand.
 */
@Entity(primaryKeys = ["dateTime"])
class Data {
    var value: String? = null
    var dateTime: String = ""
}
