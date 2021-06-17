package com.example.compose.rally.data.bill

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Query("SELECT * FROM bill_table ORDER BY date DESC")
    fun getBills(): Flow<List<BillData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(billData: BillData)

    @Update
    suspend fun update(billData: BillData)

    @Delete
    suspend fun delete(billData: BillData)

}
