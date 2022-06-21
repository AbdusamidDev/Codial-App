package developer.abdusamid.codial_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import developer.abdusamid.codial_app.Object.Object
import developer.abdusamid.codial_app.adapters.GroupsAdapter
import developer.abdusamid.codial_app.databinding.DialogDeleteBinding
import developer.abdusamid.codial_app.databinding.DialogEditGroupsBinding
import developer.abdusamid.codial_app.databinding.FragmentItemBinding
import developer.abdusamid.codial_app.db.MyDBHelper
import developer.abdusamid.codial_app.models.Groups
import developer.abdusamid.codial_app.models.Mentors

class ItemFragment : Fragment() {
    lateinit var myDBHelper: MyDBHelper
    lateinit var binding: FragmentItemBinding
    lateinit var bindingDelete: DialogDeleteBinding
    lateinit var bindingDialog: DialogEditGroupsBinding
    lateinit var groupsAdapter: GroupsAdapter
    lateinit var arrayListGroups: ArrayList<Groups>
    lateinit var hashSet: HashSet<String>
    lateinit var dialog: AlertDialog
    lateinit var dialogDelete: AlertDialog
    lateinit var arrayListMentors: ArrayList<Mentors>
    lateinit var arrayListMentorsString: ArrayList<String>
    lateinit var arrayListTime: ArrayList<String>
    lateinit var arrayListDay: ArrayList<String>
    lateinit var arrayAdapterTimes: ArrayAdapter<String>
    lateinit var arrayAdapterDays: ArrayAdapter<String>
    lateinit var arrayAdapterMentors: ArrayAdapter<String>
    var booleanAntiBag = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
        binding.recyclerViewGroups.adapter = groupsAdapter
    }

    private fun loadData() {
        myDBHelper = MyDBHelper(requireActivity())
        arrayListGroups = ArrayList()
        hashSet = HashSet()
        arrayListGroups = myDBHelper.showGroups("0", requireActivity())
        val arrayList = ArrayList<Groups>()
        for (i in arrayListGroups) {
            if (i.courses!!.id == Object.courses.id) {
                arrayList.add(i)
            }
            hashSet.add(i.name!!)
        }
        arrayListGroups = arrayList
        groupsAdapter = GroupsAdapter(requireActivity(),
            arrayListGroups,
            object : GroupsAdapter.RVClickGroups {
                override fun showGroups(groups: Groups) {
                    Object.groups = groups
                    findNavController().navigate(R.id.action_groupsFragment_to_showGroupsFragment)
                }

                override fun editGroups(groups: Groups, position: Int) {
                    if (booleanAntiBag) {
                        loadDataDialog(groups)
                        buildDialog(groups)
                        booleanAntiBag = false
                    }
                }

                override fun deleteGroups(groups: Groups) {
                    if (booleanAntiBag) {
                        buildDialogDelete(groups)
                        booleanAntiBag = false
                    }
                }
            })
    }

    private fun loadDataDialog(groups: Groups) {
        bindingDialog = DialogEditGroupsBinding.inflate(layoutInflater)
        arrayListMentors = ArrayList()
        arrayListMentorsString = ArrayList()
        arrayListTime = ArrayList()
        arrayListDay = ArrayList()
        Object.mentors = groups.mentors!!

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

    @SuppressLint("SetTextI18n")
    private fun buildDialog(groups: Groups) {
        val alertDialog = AlertDialog.Builder(activity)
        bindingDialog.edtGroupsName.setText(groups.name)
        bindingDialog.spinnerMentors.setText("${groups.mentors!!.name} ${groups.mentors!!.surname}")
        bindingDialog.spinnerTimes.setText(groups.times)
        bindingDialog.spinnerDays.setText(groups.days)
        bindingDialog.spinnerTimes.setAdapter(arrayAdapterTimes)
        bindingDialog.spinnerDays.setAdapter(arrayAdapterDays)
        bindingDialog.spinnerMentors.setAdapter(arrayAdapterMentors)


        bindingDialog.spinnerMentors.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    Object.mentors = arrayListMentors[position]
                }
            }

        bindingDialog.tvCancel.setOnClickListener {
            dialog.cancel()
        }

        bindingDialog.tvSave.setOnClickListener {
            if (bindingDialog.edtGroupsName.text.toString().trim()
                    .isNotEmpty() && bindingDialog.spinnerMentors.text.toString().trim()
                    .isNotEmpty() && bindingDialog.spinnerTimes.text.toString().trim().isNotEmpty()
                && bindingDialog.spinnerDays.text.toString().trim().isNotEmpty()
            ) {
                val groupID = groups.id
                val groupName = bindingDialog.edtGroupsName.text.toString().trim()
                val groupMentor = Object.mentors
                val groupTime = bindingDialog.spinnerTimes.text.toString().trim()
                val groupDay = bindingDialog.spinnerDays.text.toString().trim()
                val groupCourses = Object.courses
                val open = groups.open
                val groupsAdd = Groups(
                    groupID, groupName, groupMentor, groupTime, groupDay, groupCourses, open
                )

                val boolean = hashSet.add(groupsAdd.name!!)
                if (boolean || groupsAdd.name == groups.name) {
                    myDBHelper.updateGroups(groupsAdd, requireActivity())
                    onResume()
                    dialog.cancel()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to Added \na group with this name already exists, please try again by changing the name",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }

        alertDialog.setOnCancelListener {
            booleanAntiBag = true
        }

        alertDialog.setView(bindingDialog.root)
        dialog = alertDialog.create()
        dialog.window!!.attributes.windowAnimations = R.style.MyAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun buildDialogDelete(groups: Groups) {
        val alertDialog = AlertDialog.Builder(activity)
        bindingDelete = DialogDeleteBinding.inflate(layoutInflater)
        bindingDelete.tvDescription.text =
            "Do you want to delete this group? , if you delete a group, the students associated with it will also be deleted!"

        bindingDelete.tvCancel.setOnClickListener {
            dialogDelete.cancel()
        }

        bindingDelete.tvDelete.setOnClickListener {
            val boolean = myDBHelper.deleteGroups(groups)
            if (boolean) {
                Toast.makeText(requireActivity(), "Successfully Delete!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Failed to Delete", Toast.LENGTH_SHORT).show()
            }
            dialogDelete.cancel()
            onResume()
        }

        alertDialog.setOnCancelListener {
            booleanAntiBag = true
        }

        alertDialog.setView(bindingDelete.root)
        dialogDelete = alertDialog.create()
        dialogDelete.window!!.attributes.windowAnimations = R.style.MyAnimation
        dialogDelete.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogDelete.show()
    }

}