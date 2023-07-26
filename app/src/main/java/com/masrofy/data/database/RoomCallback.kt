package com.masrofy.data.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masrofy.data.entity.CategoryEntity
import com.masrofy.data.entity.defaultAccount
import com.masrofy.model.TransactionCategory
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
            val getCategories = database.categoryDao.getCategories()
            if (getCategories.isEmpty()){
                val toCategoryEntity = TransactionCategory.values().map {
                    CategoryEntity(0,it.nameCategory,it.type.name,true,0)
                }
                database.categoryDao.upsertCategory(toCategoryEntity)

            }
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        val database = this.db.get()
        CoroutineScope(Job()).launch {
            val getCategories = database.categoryDao.getCategories()
            if (getCategories.isEmpty()){
                val toCategoryEntity = TransactionCategory.values().map {
                    CategoryEntity(0,it.nameCategory,it.type.name,true,0)
                }
                database.categoryDao.upsertCategory(toCategoryEntity)
            }
        }
    }
}