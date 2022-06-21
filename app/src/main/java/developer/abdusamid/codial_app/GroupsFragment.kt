package developer.abdusamid.codial_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import developer.abdusamid.codial_app.Object.Object
import developer.abdusamid.codial_app.adapters.ViewPagerAdapter
import developer.abdusamid.codial_app.databinding.FragmentGroupsBinding
import developer.abdusamid.codial_app.db.MyDBHelper

class GroupsFragment : Fragment() {

    lateinit var binding: FragmentGroupsBinding
    lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var myDBHelper: MyDBHelper
    private var arrayListFragment = arrayListOf<Fragment>()
    private lateinit var arrayListTypes: ArrayList<String>
    lateinit var arrayListViewPager: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        loadData()
        binding.viewPager.adapter = viewPagerAdapter
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.lyAdd.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_addGroupsFragment)
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = arrayListTypes[position]
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.imageAdd.visibility = View.INVISIBLE
                        binding.lyAdd.visibility = View.INVISIBLE
                    }
                    1 -> {
                        binding.imageAdd.visibility = View.VISIBLE
                        binding.lyAdd.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewPagerAdapter.notifyItemChanged(binding.viewPager.currentItem)
        return binding.root
    }

    private fun loadData() {
        arrayListTypes = ArrayList()
        myDBHelper = MyDBHelper(requireActivity())
        binding = FragmentGroupsBinding.inflate(layoutInflater)
        binding.tvCoursesName.text = Object.courses.name
        arrayListFragment = arrayListOf(
            ItemFragment2(), ItemFragment()
        )

        arrayListTypes.add("Ochilgan\nguruhlar")
        arrayListTypes.add("Ochilayotgan\nguruhlar")
        viewPagerAdapter =
            ViewPagerAdapter(arrayListFragment, requireActivity().supportFragmentManager, lifecycle)
    }

}