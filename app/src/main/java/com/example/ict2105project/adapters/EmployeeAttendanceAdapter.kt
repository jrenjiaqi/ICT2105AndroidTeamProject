package com.example.ict2105project.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.R
import com.example.ict2105project.entity.AttendanceRecord

class EmployeeAttendanceAdapter(val context: Context, itemArr:List<AttendanceRecord>) :
    RecyclerView.Adapter<EmployeeAttendanceAdapter.ViewHolder>() {
    var recordsArr = itemArr
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeAttendanceAdapter.ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_employee_attendance, parent, false)

        //call viewholder class to create the a new viewholder object
        return ViewHolder(view)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cardView = ItemView.findViewById<CardView>(R.id.employeeAttendanceCardView)
        val clockInTextView = ItemView.findViewById<TextView>(R.id.ClockinTextView)
        val clockOutTextView = ItemView.findViewById<TextView>(R.id.ClockoutTextView)
        val statusTextView = ItemView.findViewById<TextView>(R.id.StatusTextView)
    }

    override fun onBindViewHolder(holder: EmployeeAttendanceAdapter.ViewHolder, position: Int) {
        val item = recordsArr[position]

        val clockin = recordsArr[position].clockInTime
        val clockout = recordsArr[position].clockOutTime
        val isLate = recordsArr[position].isLate
        val status:String

        //indicate color for showing isLate (1, being late so set color red. 0, being on time so set color green)
        if (isLate == 1){
            status = "Late"
            holder.statusTextView.setTextColor(Color.parseColor("#FFF44336"))
        }else{
            status = "On Time"
            holder.statusTextView.setTextColor(Color.parseColor("#FF3B9C3F"))
        }

        holder.clockInTextView.text = clockin
        holder.clockOutTextView.text = clockout
        holder.statusTextView.text = status

        //to change the alternate row background color
        if (position % 2 == 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#FFECEAEA"))
        }
    }

    override fun getItemCount(): Int {
        return recordsArr.size
    }
}