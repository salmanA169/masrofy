package com.masrofy.data.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masrofy.data.entity.defaultAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class RoomCallback @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db:Provider<MasrofyDatabase>
):RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val defaultAccount = defaultAccount()
        val database = this.db.get()
        CoroutineScope(Job()).launch {
            database.transactionDao.addAccount(defaultAccount)
        }
    }
}