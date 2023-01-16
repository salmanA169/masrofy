package com.masrofy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.masrofy.data.converter.ConverterDate
import com.masrofy.data.dao.TransactionDao
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity

@Database(
    version = 1,
    entities = [TransactionEntity::class,AccountEntity::class]
)
@TypeConverters(value =[ConverterDate::class])
abstract class MasrofyDatabase:RoomDatabase() {
    abstract val transactionDao:TransactionDao
}