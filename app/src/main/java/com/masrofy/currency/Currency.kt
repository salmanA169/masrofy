package com.masrofy.currency

data class Currency(
    val currencyCode:String,
    val countryCode:String
){
    companion object{
        val DEFAULT_CURRENCY = Currency("USD","US")
    }
}
