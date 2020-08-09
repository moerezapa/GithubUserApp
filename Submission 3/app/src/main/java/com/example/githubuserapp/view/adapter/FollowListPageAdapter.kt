package com.example.githubuserapp.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserapp.R
import com.example.githubuserapp.view.fragment.FollowersFragment
import com.example.githubuserapp.view.fragment.FollowingFragment

class FollowListPageAdapter(private val mContext: Context, fm: FragmentManager, var userType: String) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(R.string.followingtab_title, R.string.followertab_title)

    override fun getItem(position: Int): Fragment = if (position == 0) FollowingFragment(userType) else FollowersFragment(userType)
    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(tabTitles[position])
    override fun getCount(): Int = tabTitles.size

}