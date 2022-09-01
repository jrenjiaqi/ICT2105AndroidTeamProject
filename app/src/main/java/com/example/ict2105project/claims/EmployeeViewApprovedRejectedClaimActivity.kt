package com.example.ict2105project.claims

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.adapters.EmployeeClaimRecordAdapter
import com.example.ict2105project.databinding.ActivityEmployeeViewApprovedRejectedClaimsBinding
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.viewmodel.ClaimsViewModel
import com.example.ict2105project.viewmodel.ClaimsViewModelFactory

/**
 * The Activity where the employee can view their approved and rejected claims
 */
class EmployeeViewApprovedRejectedClaimActivity : AppCompatActivity() {
    // setting up the binding
    private lateinit var binding: ActivityEmployeeViewApprovedRejectedClaimsBinding
    // setting up the sharedPreference
    private lateinit var sharedPref: SharedPreferences

    // setting up RecyclerView for approved
    private var layoutManagerApproved: RecyclerView.LayoutManager? = null
    private var adapterApproved: RecyclerView.Adapter<EmployeeClaimRecordAdapter.ViewHolder>? = null

    // setting up RecyclerView for rejected
    private var layoutManagerRejected: RecyclerView.LayoutManager? = null
    private var adapterRejected: RecyclerView.Adapter<EmployeeClaimRecordAdapter.ViewHolder>? = null

    /**
     * Sets up all the elements in the activity
     * @param savedInstanceState bundle to save the state throughout lifecycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeViewApprovedRejectedClaimsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // prepare the layout manager for approved and rejected recyclerview
        layoutManagerApproved = LinearLayoutManager(this)
        layoutManagerRejected = LinearLayoutManager(this)

        // sets the 'nothing to display' text to gone and the recyclerview to visible for approved
        binding.employeeApprovedClaimsEmptyTextView.visibility = View.GONE
        binding.employeeViewApprovedClaimsRecyclerView.visibility = View.VISIBLE

        // sets the 'nothing to display' text to gone and the recyclerview to visible for rejected
        binding.employeeRejectedClaimsEmptyTextView.visibility = View.GONE
        binding.employeeViewRejectedClaimsRecyclerView.visibility = View.VISIBLE

        // setting approved recyclerview container to layout mode
        val recyclerViewApprovedClaimsRecord: RecyclerView = findViewById(R.id.employeeViewApprovedClaimsRecyclerView)
        recyclerViewApprovedClaimsRecord.layoutManager = layoutManagerApproved
        // setting rejected recyclerview container to layout mode
        val recyclerViewRejectedClaimsRecord: RecyclerView = findViewById(R.id.employeeViewRejectedClaimsRecyclerView)
        recyclerViewRejectedClaimsRecord.layoutManager = layoutManagerRejected

        // obtain the employee id for the current employee logged-in via sharedPreferences
        sharedPref = this.getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        var eid: Int = sharedPref.getString(getString(R.string.user_eid),"")!!.toInt()

        // setting up claims view model
        val claimsViewModel: ClaimsViewModel by viewModels {
            ClaimsViewModelFactory((application as HRApp).repo)
        }

        // get the approved list of claims for the person that is currently logged in
        claimsViewModel.getListOfClaimsByEidAndStatus(eid, ClaimLeaveStatusEnum.APPROVED.status)
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        claimsViewModel.listOfApprovedClaimRecord.observe(this) { listOfClaimRecordByEid ->
            if (listOfClaimRecordByEid.isEmpty()) {
                binding.employeeApprovedClaimsEmptyTextView.visibility = View.VISIBLE
                binding.employeeViewApprovedClaimsRecyclerView.visibility = View.GONE
            }
            // reversed so most recent claims records show up on top of recyclerview.
            val reverseClaimList: List<ClaimRecord> = listOfClaimRecordByEid.reversed()
            // initialise the adapter
            adapterApproved = EmployeeClaimRecordAdapter(reverseClaimList)
            // setting recyclerview to adapter
            recyclerViewApprovedClaimsRecord.adapter = adapterApproved
        }

        // get the rejected list of claims
        claimsViewModel.getListOfClaimsByEidAndStatus(eid, ClaimLeaveStatusEnum.REJECTED.status)
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        claimsViewModel.listOfRejectedClaimRecord.observe(this) { listOfClaimRecordByEid ->
            if (listOfClaimRecordByEid.isEmpty()) {
                binding.employeeRejectedClaimsEmptyTextView.visibility = View.VISIBLE
                binding.employeeViewRejectedClaimsRecyclerView.visibility = View.GONE
            }
            // reversed so most recent claims records show up on top of recyclerview.
            val reverseClaimList: List<ClaimRecord> = listOfClaimRecordByEid.reversed()
            // initialise the adapter
            adapterRejected = EmployeeClaimRecordAdapter(reverseClaimList)
            // setting recyclerview to adapter
            recyclerViewRejectedClaimsRecord.adapter = adapterRejected
        }

        // sets up a button that refreshes the page on click
        binding.employeeClaimsApproveRejectRefreshPageButton.setOnClickListener {
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}