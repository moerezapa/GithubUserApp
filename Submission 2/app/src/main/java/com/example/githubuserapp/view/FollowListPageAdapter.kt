package com.example.githubuserapp.view

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserapp.R

class FollowListPageAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(R.string.followingtab_title, R.string.followertab_title)

    override fun getItem(position: Int): Fragment = if (position == 0) FollowingFragment() else FollowersFragment()
    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(tabTitles[position])
    override fun getCount(): Int = tabTitles.size

}