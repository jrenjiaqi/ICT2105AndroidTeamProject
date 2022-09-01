package com.example.ict2105project.claims

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.adapters.ManagerClaimRecordAdapter
import com.example.ict2105project.databinding.ActivityManagerManageAllClaimsBinding
import com.example.ict2105project.entity.ClaimRecordWithName
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.viewmodel.ManagerClaimsViewModel
import com.example.ict2105project.viewmodel.ManagerClaimsViewModelFactory

/**
 * The Activity where the manager can manage all employee claims
 */
class ManagerManageAllClaimsActivity : AppCompatActivity() {
    // setting up the binding
    private lateinit var binding: ActivityManagerManageAllClaimsBinding

    // setting up RecyclerView for all claim requests
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ManagerClaimRecordAdapter.ViewHolder>? = null

    // a list of claim requests that manager has clicked on the approve button
    private var approveList: MutableList<Int> = mutableListOf()
    // a list of rejected claim requests that manager has clicked on the reject button
    private var rejectList: MutableList<Int> = mutableListOf()

    /**
     * resets the page by restarting the activity and stopping any animations
     */
    private fun resetPage() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * Sets up all the elements in the activity
     * @param savedInstanceState bundle to save the state throughout lifecycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setting up binding
        binding = ActivityManagerManageAllClaimsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // sets the 'nothing to display' text to gone and the recyclerview to visible
        binding.managerManageClaimsEmptyTextView.visibility = View.GONE
        binding.managerManageClaimsItemRecyclerView.visibility = View.VISIBLE

        // prepare the layout manager
        layoutManager = LinearLayoutManager(this)

        // setting recyclerview container to layout mode
        binding.managerManageClaimsItemRecyclerView.layoutManager = layoutManager

        // use view model
        val managerClaimsViewModel: ManagerClaimsViewModel by viewModels {
            ManagerClaimsViewModelFactory((application as HRApp).repo)
        }
        managerClaimsViewModel.getListOfClaimRecordWithNameByStatus(ClaimLeaveStatusEnum.PENDING.status)

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        managerClaimsViewModel.listOfClaimRecordWithNameByStatus.observe(this) { listOfClaimRecordWithNameByStatus ->
            if (listOfClaimRecordWithNameByStatus.isEmpty()) {
                binding.managerManageClaimsEmptyTextView.visibility = View.VISIBLE
                binding.managerManageClaimsItemRecyclerView.visibility = View.GONE
            }
            // reversed so most recent claims records show up on top of recyclerview.
            val reversedList: List<ClaimRecordWithName> = listOfClaimRecordWithNameByStatus.reversed()
            // initialise the adapter
            adapter = ManagerClaimRecordAdapter(reversedList, approveList, rejectList)

            // setting recyclerview to adapter
            binding.managerManageClaimsItemRecyclerView.adapter = adapter
        }

        /*
         set up submit button
         for cid in approveList, set their status to enum value for approved
         for cid in rejectList, set their status to enum value for rejected
         */
        binding.managerManageClaimsSubmitApproveRejectButton.setOnClickListener {
                val builder = android.app.AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle(R.string.managerManageClaimsSubmitDialogTitle)

            val msg =  "Approving: " + approveList.count().toString() + " claim(s)." + "\n" +
                       "Rejecting: " + rejectList.count().toString() + " claim(s)." + "\n";
            //set message for alert dialog
            builder.setMessage(msg)
            //performing positive action
            builder.setPositiveButton("Confirm"){dialogInterface, which ->
//                Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                val view = View.inflate(this@ManagerManageAllClaimsActivity, R.layout.activity_manage_claims_alert, null)
                val builder = android.app.AlertDialog.Builder(this@ManagerManageAllClaimsActivity)
                builder.setView(view)

                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.setCancelable(false)

                for (cid in approveList) {
                    // add all claims items in the approveList into the database via view mode
                    managerClaimsViewModel.updateClaimStatus(cid, ClaimLeaveStatusEnum.APPROVED.status)
                }
                for (cid in rejectList) {
                    // add all claims items in the rejectList into the database via view mode
                    managerClaimsViewModel.updateClaimStatus(cid, ClaimLeaveStatusEnum.REJECTED.status)
                }

                // after clicking on the confirm button, simply dismiss the dialog
                view.findViewById<Button>(R.id.claimsManageAlertConfirmButton).setOnClickListener {
                    dialog.dismiss()
                    resetPage()
                }
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                // do nothing except cancel the dialog
            }

            // Create the AlertDialog
            val alertDialog: android.app.AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

        // set up clear button
        binding.managerManageClaimsClearSelectionButton.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(R.string.managerManageClaimsClearDialogTitle)

            val msg =  "Are you sure you wish to clear: \n" + "    - " + approveList.count() +
                    " Approval(s)\n" + "    - " + rejectList.count() + " Rejection(s)"
            //set message for alert dialog
            builder.setMessage(msg)
            //performing positive action
            builder.setPositiveButton("Confirm"){dialogInterface, which ->
                // reset the page to refresh the items on the page
                resetPage()
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                // do nothing except cancel the dialog
            }

            // Create the AlertDialog
            val alertDialog: android.app.AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}