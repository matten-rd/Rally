package com.example.compose.rally.ui.bills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.compose.rally.data.bill.BillDao
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.data.month.MonthData
import com.example.compose.rally.ui.theme.Green500
import com.example.compose.rally.ui.theme.Red300
import com.example.compose.rally.utils.ColorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@HiltViewModel
class BillViewModel @Inject constructor(
    private val billDao: BillDao
) : ViewModel() {

    val bills = billDao.getBills().asLiveData()

    fun createBill(bill: BillData) = viewModelScope.launch {
        billDao.insert(bill)
    }

    fun deleteBill(bill: BillData) = viewModelScope.launch {
        billDao.delete(bill)
    }

    fun updateBill(bill: BillData) = viewModelScope.launch {
        billDao.update(bill)
    }

    fun getBillsFormattedToMonthRow(billsList: List<BillData>): List<MonthData> {
        val formattedMonthList: MutableList<MonthData> = mutableListOf()

        billsList.groupBy {
            LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE).month
        }.forEach { (month, bills) ->
            val sumByMonth = bills.sumByDouble { it.amount.toDouble() }.toFloat()
            val monthExpenses = bills.filter { it.amount <= 0 }
                .sumByDouble { it.amount.toDouble() }.toFloat()
            val monthIncome = bills.filter { it.amount > 0 }
                .sumByDouble { it.amount.toDouble() }.toFloat()
            val color = if (sumByMonth <= 0) Red300 else Green500
            val monthString = month.toString()
            val monthData = MonthData(
                month = monthString,
                sumByMonth = sumByMonth,
                monthExpenses = monthExpenses,
                monthIncome = monthIncome,
                colorHEX = ColorConverter.getHex(color)
            )
            formattedMonthList.add(monthData)
        }

        return formattedMonthList
    }

    fun getTabTitles(): List<String>? {
        // convert dates to LocalDate objects
        val dates = bills.value?.let { billsList ->
            billsList.map { LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE) }
        }?.asReversed()
        // Format the dates to MMM YYYY and get only the unique elements
        return dates?.map {
            it.format(DateTimeFormatter.ofPattern("MMM yyyy"))
                .toUpperCase(Locale.getDefault())
        }?.distinct()
    }


    fun getBillsFilteredByMonth(tabTitle: String): List<BillData>? =
        bills.value?.let { billsList ->
            billsList.filter {
                LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
                    .format(
                        DateTimeFormatter.ofPattern("MMM yyyy")
                    ).toUpperCase(Locale.getDefault()) == tabTitle
            }
        }

    fun getBillsFilteredByAmountMonth(
        positive: Boolean, allBillsByMonth: List<BillData>?
    ): List<BillData>? =
        allBillsByMonth?.let { billsList ->
            billsList.filter {
                if(positive)
                    it.amount > 0f
                else
                    it.amount <= 0f
            }
        }


    fun getBillsFilteredByCategory(
        allBillsByMonth: List<BillData>?
    ): List<BillData> {
        val categoryList: MutableList<BillData> = mutableListOf()
        allBillsByMonth?.groupBy { it.colorHEX }?.forEach { (colorHex, bills) ->
            val sumByColor = bills.sumByDouble { it.amount.toDouble() }.toFloat()
            val category = bills.find { it.colorHEX == colorHex }?.category
            val bill = BillData(
                comment = null,
                category = category!!,
                date = "",
                amount = sumByColor,
                colorHEX = colorHex
            )
            categoryList.add(bill)
        }
        return categoryList
    }

    fun getBillsFilteredByAmountCategory(
        positive: Boolean, allBillsByCategory: List<BillData>
    ): List<BillData> =
        allBillsByCategory.filter {
            if (positive)
                it.amount > 0f
            else
                it.amount <= 0f
        }


}