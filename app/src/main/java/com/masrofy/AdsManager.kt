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
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

interface EventFullScreenAds {
    fun onDismiss()

}

private const val TEST_ID = "ca-app-pub-3940256099942544/1033173712"

class AdsManager @Inject constructor(private val context: Context) : InterstitialAdLoadCallback(), EventFullScreenAds {
    private val TAG = javaClass.simpleName
    private var mInterstitialAd: InterstitialAd? = null
    private val fullEventScreenAdsCallback = FullScreenAdsCallbackImpl(this)
    private val adsRequester = AdRequest.Builder().build()

    private fun loadAds() {
        InterstitialAd.load(context, BuildConfig.ADMOB_ADS_ID, adsRequester, this)
    }

    init {
        loadAds()
    }

    override fun onAdFailedToLoad(p0: LoadAdError) {
        super.onAdFailedToLoad(p0)
        Log.d(TAG, "onAdFailedToLoad: called $p0")
        mInterstitialAd = null
    }

    override fun onAdLoaded(p0: InterstitialAd) {
        super.onAdLoaded(p0)
        mInterstitialAd = p0
        mInterstitialAd?.fullScreenContentCallback = fullEventScreenAdsCallback
    }

    fun showAds(activity:Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        } else {
            loadAds()
        }
    }

    override fun onDismiss() {
        mInterstitialAd = null
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