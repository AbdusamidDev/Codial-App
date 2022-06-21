package developer.abdusamid.codial_app

import  android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import developer.abdusamid.codial_app.Object.Object
import developer.abdusamid.codial_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        requireActivity().window.statusBarColor = Color.parseColor("#000014")
        setOnClick()
        return binding.root
    }

    private fun setOnClick() {
        binding.cardCourses.setOnClickListener {
            fragmentNavigation(
                "Barcha kurslar ro’yxati",
                true,
                R.id.action_coursesFragment_to_showCoursesFragment
            )
        }
        binding.cardGroups.setOnClickListener {
            fragmentNavigation(
                "Barcha kurslar",
                false,
                R.id.action_coursesFragment_to_groupsFragment
            )
        }
        binding.cardMentors.setOnClickListener {
            fragmentNavigation(
                "Barcha kurslar",
                false,
                R.id.action_coursesFragment_to_mentorsFragment
            )
        }

        binding.tvCodial.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://instagram.com/codial_uz?utm_medium=copy_link")
            )
            startActivity(intent)
        }
    }

    private fun fragmentNavigation(string: String, boolean: Boolean, navigationId: Int) {
        Object.tvAllCoursesName = string
        Object.booleanAddCourses = boolean
        Object.navigationID = navigationId
        findNavController().navigate(R.id.action_homeFragment_to_coursesFragment)
    }

}