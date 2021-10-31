package com.tnt.ethiopianmoviesboxoffice.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.tnt.ethiopianmoviesboxoffice.R
import com.tnt.ethiopianmoviesboxoffice.adapters.MoviesAdapter
import com.tnt.ethiopianmoviesboxoffice.pojo.BoxOffice
import androidx.recyclerview.widget.DividerItemDecoration




class BoxOfficeFragment(private val season: String, private val layoutType: Int) :
    Fragment() {


    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private val TAG = "BoxOfficeFragment"

    companion object {
        const val SMALL_THUMBNAIL = 0
        const val LARGE_THUMBNIAL = 1
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val inflatedLayout: View = inflater.inflate(R.layout.box_office_layout, container, false)

        loadingProgressBar = inflatedLayout.findViewById(R.id.loading_progress_bar)

        moviesRecyclerView = inflatedLayout.findViewById(R.id.movie_recycler_view)
        moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            moviesRecyclerView.context,
            (moviesRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        moviesRecyclerView.addItemDecoration(dividerItemDecoration)


        loadMovies()



        return inflatedLayout
    }

    private fun loadMovies() {
        loadingProgressBar.visibility = View.VISIBLE
        Log.v(TAG, "loadMovies: started,  Loading progressbar - " + loadingProgressBar.visibility  )

        val boxOfficeDb = FirebaseDatabase.getInstance().getReference("EthiopianBoxOffice")
        boxOfficeDb.orderByChild("season").equalTo(season).limitToLast(1).get()
            .addOnSuccessListener {
                loadingProgressBar.visibility = View.GONE

                Log.v(TAG, "loadMovies: ${it.children}")

                for (dataSnapshot in it.children) {
                    val boxOffice = dataSnapshot.getValue<BoxOffice>()
                    if (boxOffice != null) {
                        moviesAdapter = MoviesAdapter(boxOffice)
                        moviesRecyclerView.adapter = moviesAdapter
                    }
                }


            }.addOnFailureListener {
                loadingProgressBar.visibility = View.GONE
                Log.v("firebase", "Error getting data", it)
                view?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Unable to connect to the internet, Please try again!",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Try Again") {
                                loadMovies()
                        }.show()
                }
            }
    }


}
