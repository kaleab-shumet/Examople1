package com.tnt.ethiopianmoviesboxoffice


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.tnt.ethiopianmoviesboxoffice.Class.NativeAdClass
import com.tnt.ethiopianmoviesboxoffice.adapters.MainAdapter
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice


class MainActivity : AppCompatActivity() {


    lateinit var interstitialAd: InterstitialAd
    private val mainActivity: MainActivity = this
    private lateinit var mainAdapter: MainAdapter
    private lateinit var mAdView: AdView
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private val TAG: String = "MainActivity"

    private var canShowAds = true

    private lateinit var moviesLoadProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        moviesLoadProgressBar = findViewById(R.id.load_movies_progress_bar)

        db = Firebase.firestore

        mainRecyclerView = findViewById(R.id.main_recycler_view)


        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        loadBannerAd()
        loadInterstitialAd()
        loadMovies(db)


    }

    private fun loadInterstitialAd() {
        interstitialAd =
            InterstitialAd(mainActivity, getString(R.string.facebook_interstitial_ad_unit))
        interstitialAd.loadAd(
            interstitialAd
                .buildLoadAdConfig()
                .withAdListener(object : InterstitialAdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {

                    }

                    override fun onAdLoaded(ad: Ad?) {

                        if(isAdLoaded()){
                            interstitialAd.show()
                        }

                    }

                    override fun onAdClicked(p0: Ad?) {

                    }

                    override fun onLoggingImpression(p0: Ad?) {

                    }

                    override fun onInterstitialDisplayed(p0: Ad?) {

                    }

                    override fun onInterstitialDismissed(p0: Ad?) {

                    }
                })
                .build()
        )
    }

    private fun loadBannerAd() {
        Log.d(TAG, "loadBannerAd: load banner running")
        mAdView = AdView(this, getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50)
        val adListener: AdListener = object : AdListener {
            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.e(
                    TAG,
                    """loadBannerAd: message: ${adError.errorMessage} - error code - ${adError.errorCode}"""
                )
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
                Log.d(TAG, "loadBannerAd: bannerLoaded")

            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(TAG, "onLoggingImpression: run")
            }
        }
        val adContainer = findViewById<View>(R.id.banner_container) as LinearLayout
        adContainer.addView(mAdView)
        Log.d(TAG, "loadBannerAd: after adContainer")
        mAdView.loadAd(mAdView.buildLoadAdConfig().withAdListener(adListener).build())
    }

    private fun loadMovies(db: FirebaseFirestore) {
        moviesLoadProgressBar.visibility = VISIBLE
        db.collection("EthiopianBoxOffice")
            .get(Source.SERVER)
            .addOnSuccessListener { result ->
                runOnUiThread {
                    moviesLoadProgressBar.visibility = GONE
                }

                for (document in result) {
                    val boxOffice = document.toObject<BoxOffice>()
                    mainAdapter = MainAdapter(this, boxOffice)
                    mainRecyclerView.adapter = mainAdapter
                    for (i in 1..4) {
                        loadNativeAds()
                    }

                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                runOnUiThread {
                    moviesLoadProgressBar.visibility = GONE

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Unable to connect to the internet, Please try again!",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Try Again") {
                            loadMovies(db)
                        }.show()

                }


            }
    }

    private fun loadNativeAds() {


        val nativeBannerAd = NativeBannerAd(this, getString(R.string.facebook_native_banner))

        val nativeAdListener: NativeAdListener = object : NativeAdListener {
            override fun onError(ad: Ad?, adError: AdError?) {
                Log.d(
                    TAG,
                    "onError: loadNativeAds - ${adError!!.errorMessage} - ${adError.errorCode}"
                )
            }

            override fun onAdLoaded(ad: Ad?) {
                Log.d(TAG, "onAdLoaded: loadNativeAds - ad loaded")

                val viewAttributes = NativeAdViewAttributes()
                    .setBackgroundColor(Color.BLACK)
                    .setTitleTextColor(Color.WHITE)
                    .setDescriptionTextColor(Color.LTGRAY)
                    .setButtonColor(Color.WHITE)
                    .setButtonTextColor(Color.BLACK)

                val adView = NativeBannerAdView.render(
                    mainActivity,
                    nativeBannerAd,
                    NativeBannerAdView.Type.HEIGHT_100,
                    viewAttributes
                )


                mainAdapter.setAd(NativeAdClass(adView))


            }

            override fun onAdClicked(ad: Ad?) {

                Log.d(TAG, "onAdClicked: ")

            }

            override fun onLoggingImpression(ad: Ad?) {

            }

            override fun onMediaDownloaded(ad: Ad?) {

            }
        }

        nativeBannerAd.loadAd(
            nativeBannerAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                .build()
        )
    }


    override fun onDestroy() {
        if (mAdView != null) {
            mAdView.destroy()
        }
        canShowAds = false
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        canShowAds = false
    }

    override fun onResume() {
        super.onResume()
        canShowAds = true
        if(isAdLoaded()){
            interstitialAd.show()
        }
    }

    private fun isAdLoaded() : Boolean{
        return if(interstitialAd != null) {
            canShowAds && interstitialAd.isAdLoaded
        } else {
            false
        }
    }

}