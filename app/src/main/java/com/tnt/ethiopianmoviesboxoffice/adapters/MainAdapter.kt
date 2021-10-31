package com.tnt.ethiopianmoviesboxoffice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tnt.ethiopianmoviesboxoffice.R
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice
import android.content.ActivityNotFoundException

import android.R.id

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.GridLayoutManager


class MainAdapter(private val boxOffice: BoxOffice) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private val horizontalScrollView: Int = 0
    private val recyclerDescription: Int = 1
    private val verticalScrollView: Int = 2

    private val extra: Int = 2

    init{
        boxOffice.monthMovies = boxOffice.monthMovies!!.reversed()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            horizontalScrollView -> {
                val horizontalMovieItem =
                    layoutInflater.inflate(R.layout.horizontal_movie_view, parent, false)
                HorizontalScrollViewHolder(horizontalMovieItem)
            }
            recyclerDescription -> {
                val recyclerDescription =
                    layoutInflater.inflate(R.layout.recycler_description, parent, false)
                RecyclerDescriptionViewHolder(recyclerDescription)
            }
            else -> {
                val mainMovieItem =
                    layoutInflater.inflate(R.layout.vertical_movie_item, parent, false)
                MainViewHolder(mainMovieItem)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            horizontalScrollView -> {
                val horizontalScrollViewHolder: HorizontalScrollViewHolder = holder as HorizontalScrollViewHolder
               /* horizontalScrollViewHolder.horizontalRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)*/

                horizontalScrollViewHolder.horizontalRecyclerView.layoutManager = GridLayoutManager(context, 2)
                horizontalScrollViewHolder.horizontalRecyclerView.adapter = HorizontalAdapter(boxOffice.yearMovies)

            }
            recyclerDescription -> {
                val recyclerDescriptionViewHolder: RecyclerDescriptionViewHolder =
                    holder as RecyclerDescriptionViewHolder
                val textStr = "Best of Month"
//                recyclerDescriptionViewHolder
//                    .recyclerDescriptionTv
//                    .text = textStr
            }
            else -> {

                val posInd = position - 1;
                if(posInd >= boxOffice.monthMovies?.size!!)return;
                val selMovie = boxOffice.monthMovies!![posInd];

                val videoId =  selMovie.videoId

                val mainViewHolder: MainViewHolder = holder as MainViewHolder
                val titleStr = selMovie.title
                val imageUrlStr = selMovie.imageUrl
                mainViewHolder.movieTitleTv.text = titleStr

                val countStr = "#"+ (boxOffice.monthMovies!!.size - posInd).toString()
                mainViewHolder.movieCountTv.text = countStr

                Glide.with(context)
                    .load(imageUrlStr)
                    .centerCrop()
                    .into(holder.movieImageView)

                holder.movieImageView.setOnClickListener {
                    if (videoId != null) {
                        openVideo(videoId)
                    }
                }
                holder.movieTitleTv.setOnClickListener {
                    if (videoId != null) {
                        openVideo(videoId)
                    }
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return boxOffice.monthMovies!!.size + extra
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> recyclerDescription
            itemCount - 1 -> horizontalScrollView
            else -> verticalScrollView
        }
    }

    fun openVideo(id: String){
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImageView: ImageView = itemView.findViewById(R.id.vertical_movie_image_view)
        val movieTitleTv: TextView = itemView.findViewById(R.id.vertical_movie_title_tv)
        val movieCountTv: TextView = itemView.findViewById(R.id.count_tv)
    }

    class HorizontalScrollViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {

        val horizontalRecyclerView: RecyclerView =
            itemView.findViewById(R.id.horizontal_recycler_view)
        val recyclerDescription: TextView = itemView.findViewById(R.id.recycler_description)
    }

    class RecyclerDescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val recyclerDescriptionTv: TextView = itemView.findViewById(R.id.recycler_description_tv)
    }


}
