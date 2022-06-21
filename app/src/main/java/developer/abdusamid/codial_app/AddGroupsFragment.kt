package developer.abdusamid.codial_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import developer.abdusamid.codial_app.Object.Object
import developer.abdusamid.codial_app.databinding.FragmentAddGroupsBinding
import developer.abdusamid.codial_app.db.MyDBHelper
import developer.abdusamid.codial_app.models.Groups
import developer.abdusamid.codial_app.models.Mentors

class AddGroupsFragment : Fragment() {
    lateinit var binding: FragmentAddGroupsBinding
    lateinit var myDBHelper: MyDBHelper
    lateinit var arrayListMentors: ArrayList<Mentors>
    private lateinit var arrayListMentorsString: ArrayList<String>
    private lateinit var arrayListTime: ArrayList<String>
    private lateinit var arrayListDay: ArrayList<String>
    private lateinit var arrayAdapterTimes: ArrayAdapter<String>
    private lateinit var arrayAdapterDays: ArrayAdapter<String>
    private lateinit var arrayAdapterMentors: ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        loadData()

        binding.spinnerTimes.setAdapter(arrayAdapterTimes)
        binding.spinnerDays.setAdapter(arrayAdapterDays)
        binding.spinnerMentors.setAdapter(arrayAdapterMentors)

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.spinnerMentors.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                Object.mentors = arrayListMentors[position]
            }
        }

        binding.imageSave.setOnClickListener {
            if (binding.edtGroupsName.text.toString().trim()
                    .isNotEmpty() && binding.spinnerMentors.text.toString().trim()
                    .isNotEmpty() && binding.spinnerTimes.text.toString().trim().isNotEmpty()
                && binding.spinnerDays.text.toString().trim().isNotEmpty()
            ) {
                saveGroups()
            }
        }

        return binding.root
    }

    private fun saveGroups() {
        val groupName = binding.edtGroupsName.text.toString().trim()
        val groupMentor = Object.mentors
        val groupTime = binding.spinnerTimes.text.toString().trim()
        val groupDay = binding.spinnerDays.text.toString().trim()
        val groupCourses = Object.courses
        val open = false
        val groups = Groups(
            groupName, groupMentor, groupTime, groupDay, groupCourses, open
        )
        val boolean = myDBHelper.addGroups(groups, requireActivity())
        if (boolean) {
            binding.edtGroupsName.text!!.clear()
            binding.spinnerMentors.text.clear()
            binding.spinnerTimes.text.clear()
            binding.spinnerDays.text.clear()
        }
    }

    private fun loadData() {
        binding = FragmentAddGroupsBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(requireActivity())
        arrayListMentors = ArrayList()
        arrayListMentorsString = ArrayList()
        arrayListTime = ArrayList()
        arrayListDay = ArrayList()
        arrayListTime.add("10:00 - 12:00")
        arrayListTime.add("12:00 - 14:00")
        arrayListTime.add("14:00 - 16:00")
        arrayListTime.add("16:00 - 18:00")
        arrayListTime.add("18:00 - 20:00")
        arrayListDay.add("Duyshanba/Chorshanba/Juma")
        arrayListDay.add("Seshanba/Payshanba/Shanba")

        arrayListMentors = myDBHelper.getAllMentorsByID(Object.courses.id!!)
        for (i in 0 until arrayListMentors.size) {
            arrayListMentorsString.add("${arrayListMentors[i].name!!} ${arrayListMentors[i].surname!!}")
        }

        arrayAdapterTimes = ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListTime)
        arrayAdapterDays = ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListDay)
        arrayAdapterMentors =
            ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListMentorsString)
    }

}