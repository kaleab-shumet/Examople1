package com.tnt.ethiopianmoviesboxoffice.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tnt.ethiopianmoviesboxoffice.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, private val fragmentHash: HashMap<String, Fragment>,fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val fragmentKey: String = fragmentHash.keys.toList()[position]
        return fragmentHash[fragmentKey]!!


    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentHash.keys.toList()[position]
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}