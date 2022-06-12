package com.tnt.ethiopianmoviesboxoffice


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tnt.ethiopianmoviesboxoffice.Class.NativeAdClass
import com.tnt.ethiopianmoviesboxoffice.NewPojo.FirebaseResponse
import com.tnt.ethiopianmoviesboxoffice.adapters.MainAdapter
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice
import com.tnt.ethiopianmoviesboxoffice.pojo.MonthMovies
import com.tnt.ethiopianmoviesboxoffice.pojo.Statistics
import com.tnt.ethiopianmoviesboxoffice.pojo.YearMovies
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {


    lateinit var interstitialAd: InterstitialAd
    private val mainActivity: MainActivity = this
    private lateinit var mainAdapter: MainAdapter
    private lateinit var mAdView: AdView
    private lateinit var mainRecyclerView: RecyclerView
    private val TAG: String = "MainActivity"

    private var canShowAds = true

    private lateinit var moviesLoadProgressBar: ProgressBar

    private var actionAfterAd: ActionAfterAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesLoadProgressBar = findViewById(R.id.load_movies_progress_bar)


        mainRecyclerView = findViewById(R.id.main_recycler_view)


        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        loadBannerAd()
        loadInterstitialAd()

        loadMovies()


    }

    fun loadInterstitialAd() {
        interstitialAd =
            InterstitialAd(mainActivity, getString(R.string.facebook_interstitial_ad_unit))
        interstitialAd.loadAd(
            interstitialAd
                .buildLoadAdConfig()
                .withAdListener(object : InterstitialAdListener{
                    override fun onError(p0: Ad?, p1: AdError?) {
                        if (p1 != null) {
                            Log.e(TAG, "onError: Interstitial Ad Error - "+p1.errorMessage )
                            //Toast.makeText(mainActivity, p1.errorMessage, Toast.LENGTH_LONG).show()
                        }
                       if(actionAfterAd != null) actionAfterAd!!.Action()
                    }

                    override fun onAdLoaded(p0: Ad?) {
                    }

                    override fun onAdClicked(p0: Ad?) {
                        //if(actionAfterAd != null) actionAfterAd!!.Action()
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                    }

                    override fun onInterstitialDisplayed(p0: Ad?) {
                    }

                    override fun onInterstitialDismissed(p0: Ad?) {
                        if(actionAfterAd != null) actionAfterAd!!.Action()
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

    private fun loadMovies() {
        moviesLoadProgressBar.visibility = VISIBLE

        val client: OkHttpClient = OkHttpClient.Builder()
            .build()


        val request: Request = Request.Builder()
            .url("https://firestore.googleapis.com/v1/projects/ethiopian-movies-box-office/databases/(default)/documents/EthiopianBoxOffice")
            .build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Log.e(TAG, "Error getting documents.", e)
                runOnUiThread {
                    moviesLoadProgressBar.visibility = GONE

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Unable to connect to the internet, Please try again!",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Try Again") {
                            loadMovies()
                        }.show()

                }


            }

            override fun onResponse(call: Call, response: Response) {

                try {

                    val resp: String = response.body!!.string()
                    Log.d(TAG, "onResponse: $resp")
                    val firebaseResponse: FirebaseResponse =
                        Gson().fromJson(resp, FirebaseResponse::class.java)

                    val yearMovies: ArrayList<YearMovies> = ArrayList()
                    for (yearMoviesArrayVal in firebaseResponse.documents[0]?.fields?.yearMovies?.arrayValue?.values!!) {
                        try {
                            val publishTime =
                                yearMoviesArrayVal?.mapValue?.fields?.publishTime?.stringValue
                            val imageUrl = yearMoviesArrayVal?.mapValue?.fields?.imageUrl?.stringValue
                            val videoId = yearMoviesArrayVal?.mapValue?.fields?.videoId?.stringValue
                            val title = yearMoviesArrayVal?.mapValue?.fields?.title?.stringValue
                            val thumbnailUrl =
                                yearMoviesArrayVal?.mapValue?.fields?.thumbnailUrl?.stringValue
                            val stat = yearMoviesArrayVal?.mapValue?.fields?.statistics?.mapValue?.fields

                            val statistics = Statistics(
                                stat?.likeCount?.stringValue,
                                stat?.viewCount?.stringValue,
                                stat?.favoriteCount?.stringValue,
                                stat?.commentCount?.stringValue
                            )
                            yearMovies.add(
                                YearMovies(
                                    publishTime,
                                    imageUrl,
                                    videoId,
                                    title,
                                    thumbnailUrl,
                                    statistics
                                )
                            )

                        } catch (e: Exception) {

                        }


                    }

                    val createdTime: Long? =
                        firebaseResponse.documents[0]?.fields?.createdTime?.integerValue?.toLongOrNull()

                    val monthMovies: ArrayList<MonthMovies> = ArrayList()
                    for (monthMoviesVal in firebaseResponse.documents[0]?.fields?.monthMovies?.arrayValue?.values!!) {

                        try {
                            val publishTime = monthMoviesVal?.mapValue?.fields?.publishTime?.stringValue
                            val imageUrl = monthMoviesVal?.mapValue?.fields?.imageUrl?.stringValue
                            val videoId = monthMoviesVal.mapValue?.fields?.videoId?.stringValue
                            val title = monthMoviesVal?.mapValue?.fields?.title?.stringValue
                            val thumbnailUrl =
                                monthMoviesVal?.mapValue?.fields?.thumbnailUrl?.stringValue
                            val stat = monthMoviesVal?.mapValue?.fields?.statistics?.mapValue?.fields

                            val statistics = Statistics(
                                stat?.likeCount?.stringValue,
                                stat?.viewCount?.stringValue,
                                stat?.favoriteCount?.stringValue,
                                stat?.commentCount?.stringValue
                            )

                            monthMovies.add(
                                MonthMovies(
                                    publishTime,
                                    imageUrl,
                                    videoId,
                                    title,
                                    thumbnailUrl,
                                    statistics
                                )
                            )

                        } catch (e: Exception) {

                        }


                    }

                    val uuid: String = firebaseResponse.documents[0].fields.uuid.stringValue

                    val boxOffice = BoxOffice(
                        yearMovies = yearMovies,
                        createdTime = createdTime,
                        monthMovies = monthMovies,
                        uuid = uuid
                    )


                    mainAdapter = MainAdapter(mainActivity, boxOffice)

                    runOnUiThread {
                        moviesLoadProgressBar.visibility = GONE
                        mainRecyclerView.adapter = mainAdapter
                        loadNativeAds()
                    }

                } catch (e: Exception) {

                }


            }
        })

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
        /*if (isAdLoaded()) {
            interstitialAd.show()
        }*/
    }

    fun isAdLoaded(): Boolean {
        return if (interstitialAd != null) {
            canShowAds && interstitialAd.isAdLoaded
        } else {
            false
        }
    }

    fun setActionAfterAd(actionAfterAd: ActionAfterAd){
        this.actionAfterAd = actionAfterAd
    }



}