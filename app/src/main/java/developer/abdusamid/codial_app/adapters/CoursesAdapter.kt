package developer.abdusamid.codial_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.abdusamid.codial_app.models.Courses
import developer.abdusamid.codial_app.databinding.ItemCoursesBinding

class CoursesAdapter(var arrayListCourses: ArrayList<Courses>, var rvClickCourses: RVClickCourses) :
    RecyclerView.Adapter<CoursesAdapter.VH>() {

    inner class VH(var itemRV: ItemCoursesBinding) : RecyclerView.ViewHolder(itemRV.root) {
        fun onBind(courses: Courses) {
            itemRV.tvNameCourses.text = courses.name
            itemRV.root.setOnClickListener {
                rvClickCourses.onClick(courses)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemCoursesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(arrayListCourses[position])
    }

    override fun getItemCount(): Int = arrayListCourses.size

    interface RVClickCourses {
        fun onClick(courses: Courses)
    }
}