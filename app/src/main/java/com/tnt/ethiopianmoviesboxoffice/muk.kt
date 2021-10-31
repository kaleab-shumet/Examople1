package com.tnt.ethiopianmoviesboxoffice

import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.sdk.InterstitialListener
import com.ironsource.mediationsdk.logger.IronSourceError

class muk internal constructor() {
    init {
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {}
            override fun onInterstitialAdLoadFailed(ironSourceError: IronSourceError) {}
            override fun onInterstitialAdOpened() {}
            override fun onInterstitialAdClosed() {}
            override fun onInterstitialAdShowSucceeded() {}
            override fun onInterstitialAdShowFailed(ironSourceError: IronSourceError) {}
            override fun onInterstitialAdClicked() {}
        })
    }
}