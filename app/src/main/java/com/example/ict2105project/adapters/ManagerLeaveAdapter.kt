package com.example.ict2105project.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.R
import com.example.ict2105project.entity.LeaveRecordWithName
//adapter for manager manage leave recyclerview
class ManagerLeaveAdapter(private val leaveRecord: List<LeaveRecordWithName>,private val approveList: MutableList<Int>,
                          private val rejectList: MutableList<Int>) : RecyclerView.Adapter<ManagerLeaveAdapter.ViewHolder>() {
    // inflate cardview layout to objects
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_manager_leaves, parent, false)
        //call viewholder class to create the a new viewholder object
        return ViewHolder(view)
    }

    // match the data
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val approveLeaveManagerImageButton = ItemView.findViewById<ImageView>(R.id.approveLeaveManagerImageButton)
        val rejectLeaveManagerImageButton = ItemView.findViewById<ImageView>(R.id.rejectLeaveManagerImageButton)

        val leaveIdManagerApproveTextView = ItemView.findViewById<TextView>(R.id.leaveIdManagerApproveTextView)
        val leaveDateFromManagerApproveTextView = ItemView.findViewById<TextView>(R.id.leaveDateFromManagerApproveTextView)
        val leaveDateToManagerAppoveTextView = ItemView.findViewById<TextView>(R.id.leaveDateToManagerAppoveTextView)
        val leaveTypeManagerApproveTextView = ItemView.findViewById<TextView>(R.id.leaveTypeManagerApproveTextView)
        val leaveEidManagerApproveTextView = ItemView.findViewById<TextView>(R.id.leaveEidManagerApproveTextView)
        val leaveNameManagerApproveTextView = ItemView.findViewById<TextView>(R.id.leaveNameManagerApproveTextView)
        val leaveReasonManagerApproveTextView = ItemView.findViewById<TextView>(R.id.leaveReasonManagerApproveTextView)

        val cardView = ItemView.findViewById<CardView>(R.id.managerLeaveCardView)
        
    }

    // binds the list items (data) to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lid = leaveRecord[position].leaveRecord.lid
        val dateFrom = leaveRecord[position].leaveRecord.startDate
        val dateTo = leaveRecord[position].leaveRecord.endDate
        val leaveType = leaveRecord[position].leaveRecord.leaveType
        val eid = leaveRecord[position].leaveRecord.eid
        val name = leaveRecord[position].ename
        val reason = leaveRecord[position].leaveRecord.reason


        holder.leaveIdManagerApproveTextView.text = lid.toString()
        holder.leaveDateFromManagerApproveTextView.text = dateFrom
        holder.leaveDateToManagerAppoveTextView.text = dateTo
        holder.leaveTypeManagerApproveTextView.text = leaveType
        holder.leaveEidManagerApproveTextView.text = eid.toString()
        holder.leaveNameManagerApproveTextView.text = name
        holder.leaveReasonManagerApproveTextView.text = reason
        //logic when manage click approve leave. toggle button color to show the reaction after select
        holder.approveLeaveManagerImageButton.setOnClickListener {
            val lid = leaveRecord[position].leaveRecord.lid
            // append to approve list only if not already in either lists
            if ((lid !in approveList) && (lid !in rejectList)) {
                holder.rejectLeaveManagerImageButton.setImageResource(R.drawable.ic_grey_button)
                approveList.add(lid)
            } else if ((lid in approveList) && (lid !in rejectList)) {
                holder.rejectLeaveManagerImageButton.setImageResource(R.drawable.ic_reject_leave)
                holder.rejectLeaveManagerImageButton.setImageResource(R.drawable.ic_baseline_clear_24)
                approveList.remove(lid)
            } else if ((lid !in approveList) && (lid in rejectList)) {

            } else {
                throw Exception("Approve and reject claims invalid element!")
            }
            println(">>> Approve: " + approveList)
        }
        //logic when manage click reject leave. toggle button color to show the reaction after select
        holder.rejectLeaveManagerImageButton.setOnClickListener {
            val lid = leaveRecord[position].leaveRecord.lid
            // append to approve list only if not already in either lists
            if ((lid !in rejectList) && (lid !in approveList)) {
                holder.approveLeaveManagerImageButton.setImageResource(R.drawable.ic_grey_button)

                rejectList.add(lid)
            } else if ((lid in rejectList) && (lid !in approveList)) {
                holder.approveLeaveManagerImageButton.setColorFilter(null)
                holder.approveLeaveManagerImageButton.setImageResource(R.drawable.ic_approve_leave)
                holder.approveLeaveManagerImageButton.setImageResource(R.drawable.ic_check_leave)
                rejectList.remove(lid)
            } else if ((lid !in rejectList) && (lid in approveList)) {

            } else {
                throw Exception("Approve and reject claims invalid element!")
            }
            println(">>> Reject: " + rejectList)

        }

        if (position % 2 == 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#F9F9F9"))
        }

    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return leaveRecord.size
    }

}