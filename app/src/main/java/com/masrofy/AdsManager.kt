package com.masrofy

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.masrofy.utils.datastore
import com.masrofy.utils.getCurrentCountFlow
import com.masrofy.utils.updateCurrentAdsCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

interface EventFullScreenAds {
    fun onDismiss()
}

private const val TEST_ID = "ca-app-pub-3940256099942544/5354046379"
private const val TESTINS_ID = "ca-app-pub-3940256099942544/1033173712"
class AdsChecker(private val conditionTimes: Int = 5,context: Context) {
    private var shouldShowAds: Boolean = false
    private val dataStore = context.datastore
    private var currentTimes = 0
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    init {
        coroutineScope.launch {
            dataStore.getCurrentCountFlow().collect{
                currentTimes = it
                shouldShowAds = it == 1
            }
        }
    }
    private fun resetTimes() {
        coroutineScope.launch {
            dataStore.updateCurrentAdsCount(1)
        }
    }

    private fun incrementTimes() {
        if (currentTimes >= conditionTimes) {
            resetTimes()
        } else {
            coroutineScope.launch {
                dataStore.updateCurrentAdsCount(++currentTimes)
            }
        }

    }

    fun checkConditions(): Boolean {
        incrementTimes()
        // TODO: fix it later to sync
        return shouldShowAds
    }
}

class AdsManager @Inject constructor(private val context: Context) : InterstitialAdLoadCallback(),
    EventFullScreenAds {
    private val TAG = javaClass.simpleName
    private var rewardedAd: InterstitialAd? = null
    private val fullEventScreenAdsCallback = FullScreenAdsCallbackImpl(this)
    private val adsRequester = AdRequest.Builder().build()
    private val adsChecker = AdsChecker(context = context)

    private fun loadAds() {
        InterstitialAd.load(context, if (BuildConfig.DEBUG) TESTINS_ID else BuildConfig.ADMOB_ADS_ID, adsRequester, this)
    }

    init {
        loadAds()
    }

    override fun onAdFailedToLoad(p0: LoadAdError) {
        super.onAdFailedToLoad(p0)
        Log.d(TAG, "onAdFailedToLoad: called $p0")
        rewardedAd = null
    }


    override fun onAdLoaded(p0: InterstitialAd) {
        super.onAdLoaded(p0)
        rewardedAd = p0
        rewardedAd?.fullScreenContentCallback = fullEventScreenAdsCallback
    }

    fun showAds(activity: Activity) {
        if (!BuildConfig.DEBUG){
            if (rewardedAd != null) {
                if (adsChecker.checkConditions()) {
                    rewardedAd?.show(activity)
                }
            } else {
                loadAds()
            }
        }
    }

    override fun onDismiss() {
        rewardedAd = null
        loadAds()
    }
}

class FullScreenAdsCallbackImpl(private val adsEvent: EventFullScreenAds) :
    FullScreenContentCallback() {
    private val TAG = javaClass.simpleName

    override fun onAdClicked() {
        super.onAdClicked()
        Log.d(TAG, "onAdClicked: called")
    }

    override fun onAdDismissedFullScreenContent() {
        super.onAdDismissedFullScreenContent()
        adsEvent.onDismiss()
        Log.d(TAG, "onAdDismissedFullScreenContent: called")
    }

    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
        super.onAdFailedToShowFullScreenContent(p0)
        adsEvent.onDismiss()
        Log.d(TAG, "onAdFailedToShowFullScreenContent: called with error $p0")
    }

    override fun onAdImpression() {
        super.onAdImpression()
        Log.d(TAG, "onAdImpression: called")
    }

    override fun onAdShowedFullScreenContent() {
        super.onAdShowedFullScreenContent()
        Log.d(TAG, "onAdShowedFullScreenContent: called")
    }
}