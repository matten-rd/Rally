package com.example.compose.rally.utils

import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.ui.theme.*
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

val csvData = "3260,CSN,Inkomst,2020-09-01\n" +
        "-800,Transport,Tanka,2020-09-01\n" +
        "-49,Tele & Prenum,Spotify,2020-09-01\n" +
        "2000,Gåva,Inkomst,2020-09-01\n" +
        "-134,Hälsa,Hårvax,2020-09-01\n" +
        "-110,Skola,TT bokus,2020-09-01\n" +
        "-26,Transport,Easypark,2020-09-01\n" +
        "-365,Skola,THS-avgift,2020-10-01\n" +
        "-250,Hälsa,Frisör,2020-10-01\n" +
        "-17,Mat & Dryck,Tuggummi,2020-10-01\n" +
        "-24,Transport,Parkering,2020-10-01\n" +
        "-379,Hälsa,Linser,2020-10-01\n" +
        "-106,Transport,Biltvätt,2020-10-01\n" +
        "-135,Transport,Biltvättmedel,2020-10-01\n" +
        "-165,Mat & Dryck,Prime Burger,2020-10-01\n" +
        "-49,Tele & Prenum,Spotify,2020-10-01\n" +
        "-505,Transport,Tanka,2020-10-01\n" +
        "3260,CSN,Inkomst,2020-10-01\n" +
        "-2523,Transport,Bilskatt,2020-10-01\n" +
        "-369,Transport,Bilbesiktning,2020-10-01\n" +
        "-249,Transport,Bilförsäkring,2020-10-01\n" +
        "-169,Tele & Prenum,Mobilabonnemang,2020-10-01\n" +
        "-15,Transport,Parkering,2020-10-01\n" +
        "-250,Hälsa,Frisör,2020-10-01\n" +
        "21,Gåva,Inkomst,2020-10-01\n" +
        "-309,Skola,Mekanik 2,2020-10-01\n" +
        "-93,Transport,Biltvätt,2020-11-01\n" +
        "-49,Tele & Prenum,Spotify,2020-11-01\n" +
        "3260,CSN,Inkomst,2020-11-01\n" +
        "453,Lön,Inkomst,2020-11-01\n" +
        "-249,Transport,Bilförsäkring,2020-11-01\n" +
        "-169,Tele & Prenum,Mobilabonnemang,2020-11-01\n" +
        "-68,Transport,Parkering,2020-11-01\n" +
        "-213,Transport,Magnet+tvätt,2020-12-01\n" +
        "-180,Övrigt,Julklapp,2020-12-01\n" +
        "-250,Hälsa,Frisör,2020-12-01\n" +
        "-56,Transport,Trängselskatt,2020-12-01\n" +
        "-956,Transport,Tanka,2020-12-01\n" +
        "-83,Transport,Biltvätt,2020-12-01\n" +
        "-219,Övrigt,Julklapp,2020-12-01\n" +
        "1293,Lön,Inkomst,2020-12-01\n" +
        "2462,CSN,Inkomst,2020-12-01\n" +
        "-249,Transport,Bilförsäkring,2020-12-01\n" +
        "-169,Tele & Prenum,Mobilabonnemang,2020-12-01\n" +
        "-1790,Hälsa,Linser,2020-12-01\n" +
        "-3768,Shopping,Skärm+fördelare,2021-01-01\n" +
        "-39,Transport,Batteri,2021-01-01\n" +
        "-150,Skola,Mobilhållare,2021-01-01\n" +
        "4742,CSN,Inkomst,2021-01-01\n" +
        "-69,Tele & Prenum,YouTube,2021-01-01\n" +
        "-1095,Shopping,Jeans Zalando,2021-01-01\n" +
        "-250,Hälsa,Frisör,2021-01-01\n" +
        "-160,Skola,Termo-formelbok,2021-01-01\n" +
        "-72,Hälsa,Hårvax,2021-01-01\n" +
        "-362,Skola,Termo-lärobok,2021-01-01\n" +
        "2298,Lön,Inkomst,2021-01-01\n" +
        "-249,Transport,Bilförsäkring,2021-01-01\n" +
        "-169,Tele & Prenum,Mobilabonnemang,2021-01-01\n" +
        "-98,Transport,Biltvätt,2021-01-01\n" +
        "-100,Skola,Maskinhandbok,2021-01-01\n" +
        "-50,Transport,Fuktslukare,2021-01-01\n" +
        "-328,Övrigt,Present Jesper,2021-01-01\n" +
        "-19,Transport,Parkering,2021-02-01\n" +
        "-45,Transport,Parkering,2021-02-01\n" +
        "-40,Skola,SKF-katalog,2021-02-01\n" +
        "-60,Transport,Parkering,2021-02-01\n" +
        "-2458,Transport,Fordonsskatt,2021-02-01\n" +
        "-69,Tele & Prenum,YouTube,2021-02-01\n" +
        "-1027,Transport,Tanka,2021-02-01\n" +
        "-105,Transport,Biltvätt,2021-02-01\n" +
        "-22,Transport,Trängselskatt,2021-02-01\n" +
        "-102,Övrigt,Diablo |||,2021-02-01\n" +
        "-150,Hälsa,Frisör,2021-02-01\n" +
        "4329,Lön,Inkomst,2021-02-01\n" +
        "3228,CSN,Inkomst,2021-02-01\n" +
        "-93,Transport,Biltvätt,2021-03-02\n" +
        "-20,Transport,Parkering,2021-03-02\n" +
        "-249,Transport,Bilförsäkring,2021-03-06\n" +
        "-169,Tele & Prenum,Mobilabonnemang,2021-03-06\n" +
        "-65,Transport,Parkering,2021-03-13\n" +
        "-69,Tele & Prenum,YouTube,2021-03-15\n" +
        "-250,Hälsa,Frisör,2021-03-16\n" +
        "-88,Transport,Biltvätt,2021-03-16\n" +
        "-95,Transport,Biltema,2021-03-16\n" +
        "-128,Transport,Trängselskatt,2021-03-16\n" +
        "-37,Transport,Parkering,2021-03-17\n" +
        "-217,Transport,Biltvätt,2021-03-20\n" +
        "-778,Skola,Differentialekvationer - bok,2021-03-24\n" +
        "1594,Lön,Inkomst,2021-03-25\n" +
        "3228,CSN,Inkomst,2021-03-25\n" +
        "-249,Transport,Bilförsäkring,2021-03-25\n" +
        "-169,Tele & Prenum,Mobilabonnemang,2021-03-25\n" +
        "-173,Hälsa,Elocon - recept,2021-03-25\n" +
        "-35,Transport,Parkering,2021-03-26\n" +
        "-52,Mat & Dryck,Willys,2021-03-26\n" +
        "-80,Transport,Biltvätt,2021-03-26\n" +
        "2000,Gåva,Inkomst,2021-03-29\n" +
        "-718,Hälsa,Linser,2021-03-29\n" +
        "-1776,Shopping,Zalando,2021-04-05\n" +
        "-1123,Shopping,MarQet,2021-04-05\n" +
        "-250,Hälsa,Frisör,2021-04-07\n" +
        "2010,Övrigt,Inkomst,2021-04-07\n" +
        "-69,Tele & Prenum,YouTube,2021-04-14\n" +
        "-91,Transport,Biltvätt,2021-04-16\n" +
        "-1025,Transport,Tanka,2021-04-19\n" +
        "-56,Transport,Trängselskatt,2021-04-19\n" +
        "-34,Transport,Parkering,2021-04-20\n"


val expenseCategories = listOf(
    "Transport", "Tele & Prenum", "Mat & Dryck",
    "Shopping", "Hälsa", "Skola", "Övrigt"
)
val expenseColors = listOf(
    Red400, Red300, Red200, Yellow500, Yellow300, Yellow200, Red50
)
val expenseItems = expenseCategories zip expenseColors

val incomeCategories = listOf("Lön", "CSN", "Gåva", "Övrigt")
val incomeColors = listOf(DarkGreen700, DarkGreen600, Green500, Green300)
val incomeItems = incomeCategories zip incomeColors

var previousTransactions = mutableListOf<BillData>()

val hej = csvReader().readAll(csvData).forEach { row ->
    // create BillData object from it: List<String>
    val incomeOrExpense = if (row[0].toFloat() < 0) expenseItems else incomeItems
    val color = incomeOrExpense.find { it.first == row[1] }!!.second
    val colorHex = ColorConverter.getHex(color)

    val trans = BillData(
        comment = row[2],
        category = row[1],
        date = row[3],
        amount = row[0].toFloat(),
        colorHEX = colorHex
    )
    previousTransactions.add(trans)
}

