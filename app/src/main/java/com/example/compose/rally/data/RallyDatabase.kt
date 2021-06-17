package com.example.compose.rally.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.compose.rally.data.account.AccountDao
import com.example.compose.rally.data.account.AccountData
import com.example.compose.rally.data.bill.BillDao
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.di.ApplicationScope
import com.example.compose.rally.utils.previousTransactions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [AccountData::class, BillData::class], version = 1)
abstract class RallyDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun billDao(): BillDao

    class Callback @Inject constructor(
        private val database: Provider<RallyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val aDao = database.get().accountDao()
            val bDao = database.get().billDao()

























            // Insert example data the first time the app is downloaded
            applicationScope.launch {
                aDao.insert(
                    AccountData(
                        name = "SPARANDE1",
                        bank = "BANK1",
                        balance = 10000f,
                        colorHEX = "004940"
                    )
                )
                aDao.insert(
                    AccountData(
                        name = "SPARANDE2",
                        bank = "BANK2",
                        balance = 10001f,
                        colorHEX = "004940"
                    )
                )
                previousTransactions.forEach {
                    bDao.insert(it)
                }

                bDao.insert(
                    BillData(
                        comment = "UTGIFT1",
                        category = "KATEGORI1",
                        date = "DATUM1",
                        amount = 1234f,
                        colorHEX = "FF6951"
                    )
                )
                bDao.insert(
                    BillData(
                        comment = "UTGIFT2",
                        category = "KATEGORI2",
                        date = "DATUM2",
                        amount = 4321f,
                        colorHEX = "FF6951"
                    )
                )

            }
        }
    }
}