package com.tnt.ethiopianmoviesboxoffice.adapters

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tnt.ethiopianmoviesboxoffice.Class.ItemsDescription
import com.tnt.ethiopianmoviesboxoffice.Class.NativeAdClass
import com.tnt.ethiopianmoviesboxoffice.Class.YearlyMoviesRecyclerData
import com.tnt.ethiopianmoviesboxoffice.MainActivity
import com.tnt.ethiopianmoviesboxoffice.R
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice
import com.tnt.ethiopianmoviesboxoffice.pojo.MonthMovies


class MainAdapter(private val mainActivity: MainActivity, boxOffice: BoxOffice) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val tag: String = "MainAdapter"

    private val itemDescriptionViewType: Int = 0
    private val monthlyMoviesViewType: Int = 1
    private val yearlyMoviesRecyclerType: Int = 2
    private val nativeAdViewType: Int = 3

    private val itemsArrayList: ArrayList<Any> = ArrayList()


    init {

        boxOffice.monthMovies = boxOffice.monthMovies!!.reversed()

        var rankCounter = 25
        for (item in boxOffice.monthMovies!!){
            item.rank = rankCounter
            rankCounter--
        }

        itemsArrayList.add(ItemsDescription(mainActivity.getString(R.string.best_of_month)))
        itemsArrayList.addAll(boxOffice.monthMovies!!)
        itemsArrayList.add(ItemsDescription(mainActivity.getString(R.string.best_of_year)))
        itemsArrayList.add(YearlyMoviesRecyclerData(boxOffice.yearMovies!!))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(this.mainActivity)
        return when (viewType) {
            yearlyMoviesRecyclerType -> {
                val horizontalMovieItem =
                    layoutInflater.inflate(R.layout.horizontal_movie_view, parent, false)
                YearlyMoviesViewHolder(horizontalMovieItem)
            }
            itemDescriptionViewType -> {
                val recyclerDescription =
                    layoutInflater.inflate(R.layout.item_list_description, parent, false)
                RecyclerDescriptionViewHolder(recyclerDescription)
            }
            nativeAdViewType -> {
                val nativeAdView =
                    layoutInflater.inflate(R.layout.native_ad_container_layout, parent, false)
                NativeAdViewHolder(nativeAdView)
            }
            else -> {
                val mainMovieItem =
                    layoutInflater.inflate(R.layout.vertical_movie_item, parent, false)
                MainViewHolder(mainMovieItem)
            }
        }
    }


    class NativeAdViewHolder(
        nativeAdView: View?
    ) : RecyclerView.ViewHolder(nativeAdView!!) {
        val nativeBannerAdContainer: LinearLayout =
            itemView.findViewById(R.id.native_banner_ad_container)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            yearlyMoviesRecyclerType -> {
                val yearlyMoviesViewHolder: YearlyMoviesViewHolder =
                    holder as YearlyMoviesViewHolder

                val yearlyMoviesRecyclerData: YearlyMoviesRecyclerData =
                    this.itemsArrayList[position] as YearlyMoviesRecyclerData

                yearlyMoviesViewHolder.horizontalRecyclerView.layoutManager =
                    GridLayoutManager(mainActivity, 2)
                yearlyMoviesViewHolder.horizontalRecyclerView.adapter =
                    YearlyMoviesAdapter(yearlyMoviesRecyclerData.yearMoviesList)

            }
            itemDescriptionViewType -> {

                val itemsDescription: ItemsDescription =
                    this.itemsArrayList[position] as ItemsDescription
                val recyclerDescriptionViewHolder: RecyclerDescriptionViewHolder =
                    holder as RecyclerDescriptionViewHolder
                recyclerDescriptionViewHolder.recyclerDescriptionTv.text =
                    itemsDescription.descriptionText
            }
            nativeAdViewType -> {

                val nativeAdClass: NativeAdClass = itemsArrayList[position] as NativeAdClass
                val nativeAdViewHolder: NativeAdViewHolder = holder as NativeAdViewHolder
                if (nativeAdViewHolder.nativeBannerAdContainer.childCount < 1)
                    nativeAdViewHolder.nativeBannerAdContainer.addView(nativeAdClass.adView)


            }
            else -> {

                val monthMovie: MonthMovies = this.itemsArrayList[position] as MonthMovies

                val videoId = monthMovie.videoId
                val mainViewHolder: MainViewHolder = holder as MainViewHolder
                val titleStr = monthMovie.title
                val imageUrlStr = monthMovie.imageUrl

                mainViewHolder.movieTitleTv.text = titleStr


                ("#"+monthMovie.rank).also { mainViewHolder.movieCountTv.text = it }

                Glide.with(mainActivity)
                    .load(imageUrlStr)
                    .centerCrop()
                    .into(holder.movieImageView)

                holder.itemView.setOnClickListener {
                    if (videoId != null) {
                        openVideo(videoId)
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return itemsArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            itemsArrayList[position] is ItemsDescription -> {
                itemDescriptionViewType
            }
            itemsArrayList[position] is YearlyMoviesRecyclerData -> {
                yearlyMoviesRecyclerType
            }
            itemsArrayList[position] is NativeAdClass -> {
                nativeAdViewType
            }
            else -> {
                monthlyMoviesViewType
            }
        }
    }

    private fun openVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            mainActivity.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            mainActivity.startActivity(webIntent)
        }
    }

    fun setAd(nativeAdClass: NativeAdClass) {
        Log.d(tag, "setAd: called")

        Log.d(tag, "setAd: before - itemsArrayList.size: "+this.itemsArrayList.size)

        for (i in 1..this.itemsArrayList.size) {
            val u = 6 * i - 1

            if (u < itemsArrayList.size) {

                if (itemsArrayList[u] !is NativeAdClass) {

                    itemsArrayList.add(u, nativeAdClass)
                    notifyItemInserted(u)
                    return

                }

            }
        }
    }


}

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val movieImageView: ImageView = itemView.findViewById(R.id.vertical_movie_image_view)
    val movieTitleTv: TextView = itemView.findViewById(R.id.vertical_movie_title_tv)
    val movieCountTv: TextView = itemView.findViewById(R.id.count_tv)
}

class YearlyMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {

    val horizontalRecyclerView: RecyclerView =
        itemView.findViewById(R.id.yearly_recycler_view)
}

class RecyclerDescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {
    val recyclerDescriptionTv: TextView = itemView.findViewById(R.id.item_description_tv)
}



