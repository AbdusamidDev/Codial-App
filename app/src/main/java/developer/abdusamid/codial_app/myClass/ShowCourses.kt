package developer.abdusamid.codial_app.myClass

import android.app.Activity
import androidx.navigation.NavController
import developer.abdusamid.codial_app.Object.Object
import developer.abdusamid.codial_app.adapters.CoursesAdapter
import developer.abdusamid.codial_app.databinding.FragmentCoursesBinding
import developer.abdusamid.codial_app.db.MyDBHelper
import developer.abdusamid.codial_app.models.Courses

class ShowCourses(
    var activity: Activity,
    var binding: FragmentCoursesBinding,
    var findNavController: NavController,
) {
    lateinit var arrayListCourses: ArrayList<Courses>
    lateinit var coursesAdapter: CoursesAdapter
    lateinit var myDBHelper: MyDBHelper

    fun showCourses() {
        loadData()
        coursesAdapter = CoursesAdapter(arrayListCourses, object : CoursesAdapter.RVClickCourses {
            override fun onClick(courses: Courses) {
                Object.courses = courses
                findNavController.navigate(Object.navigationID)
            }

        })
        binding.recyclerCourses.adapter = coursesAdapter
    }

    private fun loadData() {
        arrayListCourses = ArrayList()
        myDBHelper = MyDBHelper(activity)
        arrayListCourses = myDBHelper.showCourses()
    }
}