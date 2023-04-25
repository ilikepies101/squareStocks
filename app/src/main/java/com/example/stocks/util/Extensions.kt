package com.example.stocks.util

import java.text.DateFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale


object Extensions {

    fun Int.toFormattedDate(): String {
        val dateInMs = Date(this * 1000L)
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(dateInMs)
    }

    fun Int.toFormattedCurrencyAmount(currencyCode: String): String {
        return NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(currencyCode)
            isGroupingUsed = true
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }.format(this)
    }
}