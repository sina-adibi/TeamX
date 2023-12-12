package com.example.task.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.viewpager.widget.ViewPager
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.databinding.FragmentViewPagerBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewPager : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var postDao: PostDao
    private lateinit var deletedPostCount: LiveData<Int>


    private lateinit var mediatorDeletedPostCount: MediatorLiveData<Int>
//این فایل ها و کلاس های من در پروژه است و الان میخوام این فایل ها رو دسته بندی کنم لطفا راهنماییم کن
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = ApiScreenPagerAdapter(childFragmentManager)

        val tabLayout: TabLayout = binding.tabLayout
        tabLayout.setupWithViewPager(viewPager)

        val badge: BadgeDrawable = tabLayout.getTabAt(2)?.orCreateBadge ?: return view

        postDao = PostDatabase.getInstance(requireContext()).postDao()

        deletedPostCount = postDao.getDeletedPostCount()
        mediatorDeletedPostCount = MediatorLiveData()
        mediatorDeletedPostCount.addSource(deletedPostCount) { count ->
            mediatorDeletedPostCount.value = count
        }

        refreshDeletedPostCount(badge)

        return view
    }

    fun refreshDeletedPostCount(badge: BadgeDrawable) {
        mediatorDeletedPostCount.observe(viewLifecycleOwner) { count ->
            badge.number = count
            badge.isVisible = true
        }
    }

    fun deletePostAndUpdateCount(postId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val newCount = postDao.getDeletedPostCount().value ?: 0
            mediatorDeletedPostCount.value = newCount
        }
    }


    private inner class ApiScreenPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ApiScreenFragment()
                1 -> TimerScreen()
                2 -> DelApiScreen()
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "api"
                1 -> "Tab 2"
                2 -> "delete"
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }
    }

}
