package com.example.task.View

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.task.InfoFragment
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.databinding.FragmentViewPagerBinding
import com.example.task.utils.PostDeleteReceiver
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ViewPager : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var badge: BadgeDrawable
    private lateinit var postDao: PostDao
    private lateinit var postDeleteReceiver: PostDeleteReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private var deletedPostCount: Int=0
    private val filter = IntentFilter("com.example.app.ACTION_POST_DELETED")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewPager: ViewPager2 = binding.viewPagerScreen
        viewPager.adapter = ApiScreenPagerAdapter(childFragmentManager, lifecycle)

        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "API"
                1 -> "Tab 2"
                2 -> "Delete"
                3 -> "Info"
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }.attach()

        badge = tabLayout.getTabAt(2)?.orCreateBadge ?: return view

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCount = sharedPreferences.getInt("deletedPostCount", 0)
        badge.number = savedCount
        badge.isVisible = true

        postDeleteReceiver = PostDeleteReceiver()

        postDao = PostDatabase.getInstance(requireContext()).postDao()
        deletedPostCount = postDao.getDeletedPostCount()

        return view
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(postDeleteReceiver, filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(postDeleteReceiver)
    }

    inner class ApiScreenPagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ApiScreenFragment()
                1 -> TimerScreen()
                2 -> DelApiScreen()
                3 -> InfoFragment()
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }
    }

    inner class PostDeleteReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val deletedPostCount = intent?.getIntExtra("deletedPostCount", 0) ?: 0
            refreshDeletedPostCount(deletedPostCount)
        }
    }

    fun refreshDeletedPostCount(count: Int) {
        badge.number = count
        badge.isVisible = true
        sharedPreferences.edit().putInt("deletedPostCount", count).apply()
    }
}
