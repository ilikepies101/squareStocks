package com.example.stocks.util

import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

object Extensions {

    fun Int.toFormattedDate(): String {
        val date = Date(this * 1000L) // Multiply by 1000 to convert seconds to milliseconds
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault()).format(date)
    }

    fun Int.toFormattedCurrencyAmount(currencyCode: String): String {
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance(currencyCode)

        return currencyFormat.format(this)
    }
}