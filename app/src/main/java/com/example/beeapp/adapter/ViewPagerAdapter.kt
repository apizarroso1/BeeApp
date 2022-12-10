package com.example.beeapp.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.beeapp.fragment.ChatsFragment
import com.example.beeapp.fragment.EventsFragment


class   ViewPagerAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment) {



    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> {
                EventsFragment()
            }
            1-> { ChatsFragment()
            }
            else->{
                EventsFragment()
            }
    }

    }
}