package com.jogdand.roomtest

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


/**
 * @author Rushikesh Jogdand.
 */
class MyVm(application: Application) : AndroidViewModel(application) {

    private var dao: DataAo? = getDatabase(application)?.dao()

    var dataList: LiveData<MutableList<Data>>? = dao?.fetchData()

    fun addData(data: Data) {
        InsertAsyncTask(dao).execute(data)
    }
}

private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: DataAo?) : AsyncTask<Data, Void, Void>() {

    override fun doInBackground(vararg params: Data): Void? {
        mAsyncTaskDao?.addData(params[0])
        return null
    }
}
