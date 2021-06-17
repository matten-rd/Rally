package com.example.compose.rally.data.account

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {


    @Query("SELECT * FROM account_table ORDER BY name ASC")
    fun getAccounts(): Flow<List<AccountData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(accountData: AccountData)

    @Update
    suspend fun update(accountData: AccountData)

    @Delete
    suspend fun delete(accountData: AccountData)

}