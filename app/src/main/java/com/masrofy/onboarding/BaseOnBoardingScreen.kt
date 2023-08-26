package com.masrofy.onboarding

import android.util.Log
import com.masrofy.data.database.MasrofyDatabase

const val CURRENCY_SCREEN_TYPE = "CurrencyScreen"
const val WELCOME_SCREEN_TYPE = "welcomeScreen"

interface BaseOnBoardingScreen<D> {

    var data: D?
    var isEdited: Boolean
    val shouldShow: Boolean
    val canSkip: Boolean
    val screenType: String
    suspend fun save(): Boolean


}

class WelcomeOnboardingScreen : BaseOnBoardingScreen<Any> {
    override var data: Any?
        get() = null
        set(value) {}
    override var isEdited: Boolean
        get() = true
        set(value) {}
    override val shouldShow: Boolean
        get() = false
    override val canSkip: Boolean
        get() = true
    override val screenType: String
        get() = WELCOME_SCREEN_TYPE

    override suspend fun save(): Boolean {
        Log.d("", "save: called welcome")
        return true
    }
}

class CurrencyOnboardingScreen(private val db: MasrofyDatabase) :
    BaseOnBoardingScreen<String> {
    override var data: String? = null
    override var isEdited: Boolean = false
    override val shouldShow: Boolean
        get() = true
    override val canSkip: Boolean
        get() = false
    override val screenType: String
        get() = CURRENCY_SCREEN_TYPE


    override suspend fun save(): Boolean {
       db.transactionDao.getAccounts().forEach {
            db.transactionDao.upsertAccount(it.copy(currencyCode = data!!))
        }
        return false
    }
}