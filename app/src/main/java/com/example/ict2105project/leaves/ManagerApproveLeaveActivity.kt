package com.example.ict2105project.leaves

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.adapters.ManagerLeaveAdapter
import com.example.ict2105project.entity.LeaveRecordWithName
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.viewmodel.LeaveViewModel
import com.example.ict2105project.viewmodel.LeaveViewModelFactory


class ManagerApproveLeaveActivity : AppCompatActivity() {
    private var adapter: RecyclerView.Adapter<ManagerLeaveAdapter.ViewHolder>? = null
    private var approveList: MutableList<Int> = mutableListOf()
    private var rejectList: MutableList<Int> = mutableListOf()
    //function to reset the recycler view
    private fun resetPage() {
        // resets the page
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    //create view for manager manage leave
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_leave_recycler_view)

        val recyclerview = findViewById<RecyclerView>(R.id.managerLeaveRecyclerView)
        val managerManageLeavesEmptyTextView = findViewById<TextView>(R.id.managerManageLeavesEmptyTextView)
        //set visibility of textView and recyclerview.
        //This is to ensure that it display some text if there is no record
        managerManageLeavesEmptyTextView.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        //getting the view model
        val viewModel: LeaveViewModel by viewModels {
            LeaveViewModelFactory((application as HRApp).repo)
        }

        //manager get list of leave record by the status
        viewModel.managerGetListOfLeaveRecordWithNameByStatus(ClaimLeaveStatusEnum.PENDING.status)

        //observe the change in the live data and also to get the data
        viewModel.managerGetListOfLeaveRecordWithNameByStatus.observe(this) {
            if (it.isEmpty()) {
                managerManageLeavesEmptyTextView.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
            }
            // reversed so most recent leave records show up on top of recyclerview.
            val reversedList: List<LeaveRecordWithName> = it.reversed()
            //initialize the adapter
            adapter = ManagerLeaveAdapter(reversedList, approveList, rejectList)
            //setting recyclerview to the adapter
            recyclerview.adapter = adapter
        }

        val managerApproveLeave = findViewById<Button>(R.id.managerApproveLeaveButton)
        val managerClearLeaveSelectionButton = findViewById<Button>(R.id.managerClearLeaveSelectionButton)

        //listener for manager approve button,
        managerApproveLeave.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(R.string.leave_dialog_title)
            val msg = "Approving: " + approveList.count().toString() + " Leave(s)." + "\n" +
                    "Rejecting: " + rejectList.count().toString() + " Leave(s)." + "\n";
            //set message for alert dialog
            builder.setMessage(msg)
            //performing positive action
            builder.setPositiveButton("Confirm") { dialogInterface, which ->
                val view = View.inflate(
                    this@ManagerApproveLeaveActivity,
                    R.layout.activity_leave_alert,
                    null
                )
                val builder = AlertDialog.Builder(this@ManagerApproveLeaveActivity)
                builder.setView(view)

                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.setCancelable(false)

                val viewModel: LeaveViewModel by viewModels {
                    LeaveViewModelFactory((application as HRApp).repo)
                }
                //update the leave status to approve if in approvelist
                for (lid in approveList) {
                    viewModel.updateLeaveStatus(lid, ClaimLeaveStatusEnum.APPROVED.status)
                }
                //update the leave status to reject if in rejectlist
                for (lid in rejectList) {
                    viewModel.updateLeaveStatus(lid, ClaimLeaveStatusEnum.REJECTED.status)
                }
                dialog.dismiss()
                this.finish()
            }
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                //                Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }
        managerClearLeaveSelectionButton.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("clear selection")

            val msg =  "Are you sure you wish to clear: \n" + "    - " + approveList.count() + " Approval(s)\n" +
                    "    - " + rejectList.count() + " Rejection(s)"
            //set message for alert dialog
            builder.setMessage(msg)
            //performing positive action
            builder.setPositiveButton("Confirm"){dialogInterface, which ->
                resetPage()
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
//                Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }

            // Create the AlertDialog
            val alertDialog: android.app.AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}


