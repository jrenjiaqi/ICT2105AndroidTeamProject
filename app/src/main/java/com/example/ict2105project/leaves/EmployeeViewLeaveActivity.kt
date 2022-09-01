package com.example.ict2105project.leaves

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
import com.example.ict2105project.adapters.EmployeeLeaveAdapter
import com.example.ict2105project.R
import com.example.ict2105project.entity.LeaveRecord
import com.example.ict2105project.viewmodel.LeaveViewModel
import com.example.ict2105project.viewmodel.LeaveViewModelFactory

class EmployeeViewLeaveActivity : AppCompatActivity() {
    //leave adaptor for employee view
    private var adapter: RecyclerView.Adapter<EmployeeLeaveAdapter.ViewHolder>? = null
    private lateinit var sharedPref: SharedPreferences

    //create view for employee view leave
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_recycler_view)

        val recyclerview = findViewById<RecyclerView>(R.id.leaveRecyclerView)
        val employeeViewLeavesEmptyTextView = findViewById<TextView>(R.id.employeeViewLeavesEmptyTextView)
        employeeViewLeavesEmptyTextView.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
        recyclerview.layoutManager = LinearLayoutManager(this)

        //getting the view model
        val viewModel: LeaveViewModel by viewModels{
            LeaveViewModelFactory((application as HRApp).repo)
        }

        //shared preference to get the EmployeeID
        sharedPref = getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()

        //get the necessary data from the repo
        viewModel.getListOfLeaveRecords(eidSP)

        //observe the change in the live data and also to get the data
        viewModel.listOfLeaveRecords.observe(this){
            if (it.isEmpty()) {
                employeeViewLeavesEmptyTextView.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
            }
            // reversed so most recent leave records show up on top of recyclerview.
            val reversedList: List<LeaveRecord> = it.reversed()
            //initialize the adapter
            adapter = EmployeeLeaveAdapter(reversedList)
            //setting recyclerview to the adapter
            recyclerview.adapter = adapter
        }

        // set up a button to refresh the page on click
        val refreshButton = findViewById<Button>(R.id.employeeLeaveViewRefreshPageButton)
        refreshButton.setOnClickListener {
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}

