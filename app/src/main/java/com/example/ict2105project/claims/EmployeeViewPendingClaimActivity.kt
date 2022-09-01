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
import com.example.ict2105project.databinding.ActivityEmployeeViewPendingClaimBinding
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.viewmodel.ClaimsViewModel
import com.example.ict2105project.viewmodel.ClaimsViewModelFactory

/**
 * The Activity where the employee can view their pending claims
 */
class EmployeeViewPendingClaimActivity : AppCompatActivity() {
    // setting up the binding
    private lateinit var binding: ActivityEmployeeViewPendingClaimBinding
    // setting up the sharedPreference
    private lateinit var sharedPref: SharedPreferences

    // setting up RecyclerView for pending
    private var layoutManagerPending: RecyclerView.LayoutManager? = null
    private var adapterPending: RecyclerView.Adapter<EmployeeClaimRecordAdapter.ViewHolder>? = null

    /**
     * Sets up all the elements in the activity
     * @param savedInstanceState bundle to save the state throughout lifecycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeViewPendingClaimBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // sets the 'nothing to display' text to gone and the recyclerview to visible
        binding.employeePendingClaimsEmptyTextView.visibility = View.GONE
        binding.employeeViewPendingClaimsRecyclerView.visibility = View.VISIBLE

        // prepare the layout manager for pending recyclerview
        layoutManagerPending = LinearLayoutManager(this)

        // setting recyclerview container to layout mode
        val recyclerViewPendingClaimsRecord: RecyclerView = findViewById(R.id.employeeViewPendingClaimsRecyclerView)
        recyclerViewPendingClaimsRecord.layoutManager = layoutManagerPending

        // obtain the employee id for the current employee logged-in via sharedPreferences
        sharedPref = this.getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        var eid: Int = sharedPref.getString(getString(R.string.user_eid),"")!!.toInt()

        // set up the claims view model
        val claimsViewModel: ClaimsViewModel by viewModels {
            ClaimsViewModelFactory((application as HRApp).repo)
        }

        // get pending claim records for the person that is currently logged in
        claimsViewModel.getListOfClaimsByEidAndStatus(eid, ClaimLeaveStatusEnum.PENDING.status)
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        claimsViewModel.listOfPendingClaimRecord.observe(this) { listOfClaimRecordByEid ->
            if (listOfClaimRecordByEid.isEmpty()) {
                binding.employeePendingClaimsEmptyTextView.visibility = View.VISIBLE
                binding.employeeViewPendingClaimsRecyclerView.visibility = View.GONE
            }
            // reversed so most recent claims records show up on top of recyclerview.
            val reverseClaimList: List<ClaimRecord> = listOfClaimRecordByEid.reversed()
            // initialise the adapter
            adapterPending = EmployeeClaimRecordAdapter(reverseClaimList)
            // setting recyclerview to adapter
            recyclerViewPendingClaimsRecord.adapter = adapterPending
        }

        // sets up a button that refreshes the page on click
        binding.employeeClaimsPendingRefreshPageButton.setOnClickListener {
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}