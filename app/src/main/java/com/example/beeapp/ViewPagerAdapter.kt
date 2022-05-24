package com.example.beeapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter



class ViewPagerAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment) {

    companion object{
        private const val ARG_OBJECT = "object"
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> {GroupsFragment()}
            1-> { ChatsFragment()}
            else->{GroupsFragment()}
    }

    }
}