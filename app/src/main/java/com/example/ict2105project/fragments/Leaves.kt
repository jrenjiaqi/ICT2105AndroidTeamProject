package com.example.ict2105project.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ict2105project.leaves.EmployeeViewLeaveActivity
import com.example.ict2105project.leaves.EmployeeApplyLeaveActivity
import com.example.ict2105project.R
import com.example.ict2105project.databinding.FragmentLeaveMainBinding
import com.example.ict2105project.enum.RolesEnum
import com.example.ict2105project.leaves.EmployeeDeleteLeaveActivity
import com.example.ict2105project.leaves.ManagerApproveLeaveActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Leaves.newInstance] factory method to
 * create an instance of this fragment.
 */
class Leaves : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentLeaveMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    //inflate the fragment for leave feature
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave_main, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeaveMainBinding.bind(view)
        //get the employee detail from the shared preference
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        val roleID: Int? = sharedPref.getString("ROLE","")?.toInt()
        //button to redirect to the leave application activity
        binding.applyLeaveButton.setOnClickListener {
            val intent = Intent(activity, EmployeeApplyLeaveActivity::class.java)
            startActivity(intent)
        }
        //button to redirect to the employee view leave activity
        binding.viewLeaveButton.setOnClickListener {
            val intent = Intent(activity, EmployeeViewLeaveActivity::class.java)
            startActivity(intent)
        }
        //button to redirect to the employee delete leave activity
        binding.editDeleteLeaveButton.setOnClickListener {
            val intent = Intent(activity, EmployeeDeleteLeaveActivity::class.java)
            startActivity(intent)
        }

        //show or hide the button depending on the role of the employee
        // roleID = 3 is for staff, roleID = 2 is for manager, roleID = 1 is for admin
        if (roleID == RolesEnum.STAFF.id) {
            binding.approveLeaveButton.visibility = View.GONE
            binding.headingManagerLeavesTextView.visibility = View.GONE
            binding.headingManagerLeavesDivider.visibility = View.GONE
        } else if (roleID == RolesEnum.MANAGER.id || roleID == RolesEnum.ADMIN.id) {
            binding.approveLeaveButton.visibility = View.VISIBLE
            binding.headingManagerLeavesTextView.visibility = View.VISIBLE
            binding.headingManagerLeavesDivider.visibility = View.VISIBLE
        } else {
            throw IllegalArgumentException()
        }
        //button to redirect to the manager manage leave activity
        binding.approveLeaveButton.setOnClickListener {
            val intent = Intent(activity, ManagerApproveLeaveActivity::class.java)
            startActivity(intent)
        }



        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Leave.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Leaves().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}