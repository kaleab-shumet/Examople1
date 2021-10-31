package com.tnt.ethiopianmoviesboxoffice.adapters

import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tnt.ethiopianmoviesboxoffice.R
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice

class MoviesAdapter(private val boxOffice: BoxOffice) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val movieItem = layoutInflater.inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(movieItem)
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movieTitle = "#${position + 1} - " + boxOffice.items?.get(position)?.title
        holder.movieTitleTv.text = movieTitle


        val imageByteArray = Base64.decode(boxOffice.items?.get(position)?.image, Base64.DEFAULT)
        context?.let {
            Glide.with(it)
                .load(imageByteArray)
                .centerCrop()
                .into(holder.movieImageView)
        };

    }

    override fun getItemCount(): Int {
        return boxOffice.items?.size!!
    }


    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val movieImageView: ImageView = itemView.findViewById(R.id.movie_image_view)
        val movieTitleTv: TextView = itemView.findViewById(R.id.movie_title_tv)

    }

}
