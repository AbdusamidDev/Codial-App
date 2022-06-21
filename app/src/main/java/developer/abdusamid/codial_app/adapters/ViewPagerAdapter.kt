package developer.abdusamid.codial_app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private var arrayListFragment: ArrayList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return arrayListFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return arrayListFragment[position]
    }

    override fun getItemId(position: Int): Long {
        // generate new id
        return arrayListFragment[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        // false if item is changed
        return arrayListFragment.find { it.hashCode().toLong() == itemId } != null
    }
}