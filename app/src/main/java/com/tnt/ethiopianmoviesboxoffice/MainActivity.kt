package com.tnt.ethiopianmoviesboxoffice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tnt.ethiopianmoviesboxoffice.databinding.ActivityMainBinding
import com.tnt.ethiopianmoviesboxoffice.fragments.BoxOfficeFragment
import com.tnt.ethiopianmoviesboxoffice.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentHashMap = HashMap<String, Fragment>()
        fragmentHashMap["Box Office"] = BoxOfficeFragment("month", BoxOfficeFragment.SMALL_THUMBNAIL)
        //fragmentHashMap["Best Of The Year"] = BoxOfficeFragment("year", BoxOfficeFragment.LARGE_THUMBNIAL)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, fragmentHashMap, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)


    }
}