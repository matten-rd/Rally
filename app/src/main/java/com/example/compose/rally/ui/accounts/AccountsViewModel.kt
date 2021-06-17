package com.example.compose.rally.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.compose.rally.data.account.AccountDao
import com.example.compose.rally.data.account.AccountData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountDao: AccountDao
) : ViewModel() {


    val accounts = accountDao.getAccounts().asLiveData()

    fun createAccount(account: AccountData) = viewModelScope.launch {
        accountDao.insert(account)
    }

    fun deleteAccount(account: AccountData) = viewModelScope.launch {
        accountDao.delete(account)
    }

    fun updateAccount(account: AccountData) = viewModelScope.launch {
        accountDao.update(account)
    }





}