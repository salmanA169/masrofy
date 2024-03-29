package com.masrofy.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masrofy.data.converter.ConverterDate
import com.masrofy.data.dao.AutomatedBackupDao
import com.masrofy.data.dao.CategoryDao
import com.masrofy.data.dao.TransactionDao
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.AutomatedBackupEntity
import com.masrofy.data.entity.CategoryEntity
import com.masrofy.data.entity.TransactionEntity

@Database(
    version = 6,
    entities = [TransactionEntity::class, AccountEntity::class, CategoryEntity::class,AutomatedBackupEntity::class],
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(
        from = 2,
        to = 3,
        MasrofyDatabase.Migration2To3RenameColumn::class
    ),
        AutoMigration(
            from = 3,
            to = 4,
            MasrofyDatabase.Migration3To4RenameColumn::class
        ),AutoMigration(from = 5 , to = 6,MasrofyDatabase.Migration5To6::class)
    ],
    exportSchema = true
)
@TypeConverters(value = [ConverterDate::class])
abstract class MasrofyDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao
    abstract val categoryDao: CategoryDao
    abstract val automatedBackupDao:AutomatedBackupDao

    @DeleteColumn("CategoryEntity", columnName = "position")
    @DeleteColumn("CategoryEntity", columnName = "id")
    class Migration2To3RenameColumn() : AutoMigrationSpec

    @DeleteColumn("AccountEntity", columnName = "currencyCode")
    @DeleteColumn("AccountEntity", columnName = "countryCode")
    class Migration3To4RenameColumn() : AutoMigrationSpec


    @RenameTable(fromTableName = "AutomatedBackupEntity" , toTableName = "AutomatedBackup")
    class Migration5To6():AutoMigrationSpec
}


