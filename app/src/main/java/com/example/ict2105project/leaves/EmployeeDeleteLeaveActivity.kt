package com.example.ict2105project.leaves

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
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
import com.example.ict2105project.adapters.EmployeeDeleteLeaveAdapter
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.viewmodel.LeaveViewModel
import com.example.ict2105project.viewmodel.LeaveViewModelFactory

class EmployeeDeleteLeaveActivity : AppCompatActivity() {
    //leave adaptor for employee
    private var adapter: RecyclerView.Adapter<EmployeeDeleteLeaveAdapter.ViewHolder>? = null
    private lateinit var sharedPref: SharedPreferences

    //create view for employee delete leave
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_delete_leave_recycler_view)
        val recyclerview = findViewById<RecyclerView>(R.id.deleteleaveRecyclerView)
        val employeeManageLeavesEmptyTextView = findViewById<TextView>(R.id.employeeManageLeavesEmptyTextView)
        //set visibility of textView and recyclerview.
        //This is to ensure that it display some text if there is no record
        employeeManageLeavesEmptyTextView.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
        recyclerview.layoutManager = LinearLayoutManager(this)

        //getting the view model
        val viewModel: LeaveViewModel by viewModels {
            LeaveViewModelFactory((application as HRApp).repo)
        }

        sharedPref = getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()
        //get the list of record from the repo where the status is pending
        viewModel.getListOfPendingLeaveRecord(eidSP, ClaimLeaveStatusEnum.PENDING.status)

        //observe the change in the live data and also to get the data
        viewModel.listOfPendingLeaveRecords.observe(this) {
            //if no record, then set visibilty of text to be visible (so that it display some text)
            if (it.isEmpty()) {
                employeeManageLeavesEmptyTextView.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
            }
            adapter = EmployeeDeleteLeaveAdapter(this,it)
            //setting recyclerview to the adapter
            recyclerview.adapter = adapter
        }

        // set up a button to refresh the page on click
        val refreshButton = findViewById<Button>(R.id.employeeLeaveDeleteRefreshPageButton)
        refreshButton.setOnClickListener {
            resetPage()
        }
    }

    //function to delete the leave
    fun deleteLeave(lid:Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Are you sure delete this Information")
            .setPositiveButton("Yes"){
                    dialog,_->
                val viewModel: LeaveViewModel by viewModels {
                    LeaveViewModelFactory((application as HRApp).repo)
                }
                sharedPref = getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
                val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()
                //get the necessary data from the repo
                viewModel.deleteRecord(eidSP,lid)
                viewModel.getListOfPendingLeaveRecord(eidSP,ClaimLeaveStatusEnum.PENDING.status)
                resetPage()
                dialog.dismiss()
            }
            .setNegativeButton("No"){
                    dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    //function to reset the page
    private fun resetPage() {
        // resets the page
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}



