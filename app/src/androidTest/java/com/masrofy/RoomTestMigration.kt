package com.masrofy

import android.app.Instrumentation
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.masrofy.data.database.MasrofyDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTestMigration {

    @get:Rule
    val testHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MasrofyDatabase::class.java
    )

    @Test
    fun testMigration5To6() {
        var database = testHelper.createDatabase("masrofy", 5).apply {
            execSQL("INSERT INTO TransactionEntity VALUES (1,1,'INCOME',12345555,51111,'oooooooo','ssssss','weqwe','tttttt')")
            close()
        }


        database = testHelper.runMigrationsAndValidate("masrofy", 6, true)
        val cursor = database.query("SELECT * FROM AutomatedBackupEntity")

        Truth.assertThat(
            arrayOf(
                "id",
                "nameModel",
                "isAutoAutomated",
                "lastBackup",
                "periodSchedule",
                "shouldOnlyUsingWifi"
            )
        ).isEqualTo(cursor.columnNames)
    }
}