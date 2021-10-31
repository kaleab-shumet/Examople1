package com.tnt.ethiopianmoviesboxoffice

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.AudienceNetworkAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.BannerListener
import com.ironsource.mediationsdk.sdk.InterstitialListener
import com.tnt.ethiopianmoviesboxoffice.adapters.MainAdapter
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice
import androidx.recyclerview.widget.GridLayoutManager





class MainActivity : AppCompatActivity() {


    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private val TAG: String = "MainActivity"

    private lateinit var moviesLoadProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AudienceNetworkAds.initialize(this);

        moviesLoadProgressBar = findViewById(R.id.load_movies_progress_bar)

        db = Firebase.firestore
        adLoad()

        mainRecyclerView = findViewById(R.id.main_recycler_view)

        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        //mainRecyclerView.layoutManager = GridLayoutManager(this, 2)

        loadMovies(db)
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this);
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this);
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
                    mainRecyclerView.adapter = MainAdapter(boxOffice)
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


    private fun adLoad() {

        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
                IronSource.showInterstitial()
            }

            override fun onInterstitialAdLoadFailed(ironSourceError: IronSourceError) {}
            override fun onInterstitialAdOpened() {}
            override fun onInterstitialAdClosed() {}
            override fun onInterstitialAdShowSucceeded() {}
            override fun onInterstitialAdShowFailed(ironSourceError: IronSourceError) {}
            override fun onInterstitialAdClicked() {}
        })

        /**
         *Ad Units should be in the type of IronSource.Ad_Unit.AdUnitName, example
         */
        IronSource.init(
            this,
            "11e009b25",
            IronSource.AD_UNIT.INTERSTITIAL,
            IronSource.AD_UNIT.BANNER
        );

        IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE");
        IronSource.setMetaData("AdMob_TFCD", "false");

        IronSource.loadInterstitial();


        val bannerContainer: FrameLayout = findViewById(R.id.bannerContainer);
        val banner = IronSource.createBanner(this, ISBannerSize.BANNER)

        bannerContainer.addView(banner);

        banner.bannerListener = object : BannerListener {
            override fun onBannerAdLoaded() {}

            override fun onBannerAdLoadFailed(error: IronSourceError) {
                runOnUiThread { bannerContainer.removeAllViews() }
            }

            override fun onBannerAdClicked() {}

            override fun onBannerAdScreenPresented() {}

            override fun onBannerAdScreenDismissed() {}

            override fun onBannerAdLeftApplication() {}
        }
        IronSource.loadBanner(banner)


        IntegrationHelper.validateIntegration(this);
    }

}