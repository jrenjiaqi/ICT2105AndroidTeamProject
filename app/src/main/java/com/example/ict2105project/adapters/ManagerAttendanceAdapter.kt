package com.example.ict2105project.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.R
import com.example.ict2105project.entity.AttendanceWithName
import com.example.ict2105project.fragments.Attendance

class ManagerAttendanceAdapter(
    val context: Context, itemArr:List<AttendanceWithName>,
    fragment: Attendance
) : RecyclerView.Adapter<ManagerAttendanceAdapter.ViewHolder>(){

    var recordsArr = itemArr
    var frag = fragment


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_manager_attendance, parent, false)

        //call viewholder class to create the a new viewholder object
        return ViewHolder(view)
    }

    // match the data (cardview contain many fields /attributes)
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cardView = ItemView.findViewById<CardView>(R.id.managerAttendanceCardView)

        val employeeId = ItemView.findViewById<TextView>(R.id.employeeIdTextView)
        val employeeName = ItemView.findViewById<TextView>(R.id.employeeNameTextView)
        val clockinTime = ItemView.findViewById<TextView>(R.id.employeeClockinTextView)
        val clockoutTime = ItemView.findViewById<TextView>(R.id.employeeClockoutTextView)
        val statusTextView = ItemView.findViewById<TextView>(R.id.employeeAttendanceStatusTextView)

        var editBtn = ItemView.findViewById<ImageButton>(R.id.editAttendanceButton)
        var deleteBtn = ItemView.findViewById<ImageButton>(R.id.deleteAttendanceButton)
    }

    // binds the list items (data) to a view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ManagerAttendanceAdapter.ViewHolder, position: Int) {
        val item = recordsArr[position]

        val id = recordsArr[position].attendance.eid.toString()
        val name = recordsArr[position].ename
        val clockin = recordsArr[position].attendance.clockInTime
        val clockout = recordsArr[position].attendance.clockOutTime
        val isLate = recordsArr[position].attendance.isLate
        val status:String
        if (isLate == 1){
            status = "Late"
            holder.statusTextView.setTextColor(Color.parseColor("#FFF44336"))
        }else{
            status = "On Time"
            holder.statusTextView.setTextColor(Color.parseColor("#FF3B9C3F"))
        }

        holder.employeeId.text = id
        holder.clockinTime.text = clockin
        holder.clockoutTime.text = clockout
        holder.employeeName.text = name
        holder.statusTextView.text = status

        if (position % 2 == 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#FFECEAEA"))
        }

        holder.editBtn.setOnClickListener {

            if (frag is Attendance){
                (frag as Attendance).editAttendanceRecordDialog(item)
            }
        }

        holder.deleteBtn.setOnClickListener {
            if (frag is Attendance){
                (frag as Attendance).deleteAttendanceRecordDialog(recordsArr[position].attendance.aid.toString().toInt())
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return recordsArr.size
    }



}