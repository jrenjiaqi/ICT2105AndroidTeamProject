package com.example.ict2105project.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ict2105project.claims.EmployeeCreateNewClaimActivity
import com.example.ict2105project.R
import com.example.ict2105project.claims.EmployeeViewApprovedRejectedClaimActivity
import com.example.ict2105project.claims.EmployeeViewPendingClaimActivity
import com.example.ict2105project.claims.ManagerManageAllClaimsActivity
import com.example.ict2105project.databinding.FragmentClaimsBinding
import com.example.ict2105project.enum.RolesEnum

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Claims Fragment: shows different buttons for employee vs manager.
 */
class Claims : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // setting up binding
    private lateinit var binding: FragmentClaimsBinding
    // setting up sharedPreferences
    private lateinit var sharedPref: SharedPreferences

    /**
     * OnCreate for this fragment.
     * @param savedInstanceState the bundle for saving instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * Inflate the XML for the fragment.
     * @param inflater the LayoutInflater.
     * @param container the ViewGroup.
     * @param savedInstanceState the bundle for saving instance state.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_claims, container, false)
    }

    /**
     * Defines actions to take when the view is created for this fragment.
     * @param view the View
     * @param savedInstanceState the bundle to save instance state
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setting up the binding.
        binding = FragmentClaimsBinding.bind(view)
        // setting up the sharedPreferences.
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)

        // obtain the role ID of the current person that is logged in.
        var roleID: Int? = sharedPref.getString("ROLE","")?.toInt()

        // launch create new claim activity when pressing the create new claim button.
        binding.employeeCreateNewClaimButton.setOnClickListener {
            val intent = Intent(activity, EmployeeCreateNewClaimActivity::class.java)
            startActivity(intent)
        }
        // launch view approved and rejected claims activity when pressing the respective button.
        binding.employeeViewApprovedRejectedClaimButton.setOnClickListener {
            val intent = Intent(activity, EmployeeViewApprovedRejectedClaimActivity::class.java)
            startActivity(intent)
        }
        // launch view pending claims activity when pressing the view pending claims button.
        binding.employeeViewPendingClaimButton.setOnClickListener {
            val intent = Intent(activity, EmployeeViewPendingClaimActivity::class.java)
            startActivity(intent)
        }
        // starts the manage all claims activity when pressing the manage all claims button.
        binding.managerManageAllClaimButton.setOnClickListener {
            val intent = Intent(activity, ManagerManageAllClaimsActivity::class.java)
            startActivity(intent)
        }

        // for staff, hide all managerial action buttons
        if (roleID == RolesEnum.STAFF.id) {
            binding.managerManageAllClaimButton.visibility = View.GONE
            binding.headingManagerClaimsTextView.visibility = View.GONE
            binding.headingManagerClaimsDivider.visibility = View.GONE
        } // for manager and admin, show all managerial action button
        else if (roleID == RolesEnum.MANAGER.id || roleID == RolesEnum.ADMIN.id) {
            binding.managerManageAllClaimButton.visibility = View.VISIBLE
            binding.headingManagerClaimsTextView.visibility = View.VISIBLE
            binding.headingManagerClaimsDivider.visibility = View.VISIBLE
        } else {
            // throw an exception otherwise, programming oversight.
            throw IllegalArgumentException()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment claims.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Claims().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}