package com.tnt.ethiopianmoviesboxoffice.adapters

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tnt.ethiopianmoviesboxoffice.R
import com.tnt.ethiopianmoviesboxoffice.pojo.YearMovies

class HorizontalAdapter(private val yearMovies: List<YearMovies>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val horizontalMovieItem =
            layoutInflater.inflate(R.layout.horizontal_movie_item, parent, false)
        return HorizontalViewHolder(horizontalMovieItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val videoId = yearMovies!![position].videoId

        val horizontalViewHolder: HorizontalViewHolder = holder as HorizontalViewHolder

        val titleStr = yearMovies!![position].title

        horizontalViewHolder.horizontalMovieTv.text = titleStr
        Glide.with(context)
            .load(yearMovies?.get(position)!!.imageUrl)
            .centerCrop()
            .into(horizontalViewHolder.horizontalMovieImageView)

        horizontalViewHolder.horizontalMovieImageView.setOnClickListener {
            if (videoId != null) {
                openVideo(videoId)
            }
        }
        horizontalViewHolder.horizontalMovieTv.setOnClickListener {
            if (videoId != null) {
                openVideo(videoId)
            }
        }
    }

    override fun getItemCount(): Int {
        return yearMovies!!.size
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

    class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val horizontalMovieTv: TextView = itemView.findViewById(R.id.horizontal_movie_title_tv)
        val horizontalMovieImageView: ImageView =
            itemView.findViewById(R.id.horizontal_movie_image_view)
    }

}
