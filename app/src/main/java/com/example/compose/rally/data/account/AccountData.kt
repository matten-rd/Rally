package com.example.compose.rally.data.account

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "account_table")
@Parcelize
data class AccountData(
    val name: String,
    val bank: String,
    val balance: Float,
    val colorHEX: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable