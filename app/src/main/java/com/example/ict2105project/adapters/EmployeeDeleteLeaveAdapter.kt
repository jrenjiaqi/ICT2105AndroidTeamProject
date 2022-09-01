package com.example.ict2105project.adapters
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.R
import com.example.ict2105project.entity.LeaveRecord
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.leaves.EmployeeDeleteLeaveActivity

//adapter for employee delete leave recyclerview
class EmployeeDeleteLeaveAdapter(val context: Context,val leaveRecord: List<LeaveRecord>) : RecyclerView.Adapter<EmployeeDeleteLeaveAdapter.ViewHolder>() {
    // inflate cardview layout to object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_employee_delete_leaves, parent, false)

        //call viewholder class to create the a new viewholder object
        return ViewHolder(view)
    }

    // match the data (cardview contain many fields /attributes)
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val leaveDateFromTextView = ItemView.findViewById<TextView>(R.id.leaveDateFromEmployeeTextView)
        val leaveDateToTextView = ItemView.findViewById<TextView>(R.id.leaveDateToEmployeeTextView)
        val leaveStatusTextView = ItemView.findViewById<TextView>(R.id.leaveStatusEmployeeTextView)
        val leaveTypeEmployeeViewTextView = ItemView.findViewById<TextView>(R.id.leaveTypeEmployeeViewTextView)
        val ReasonEmployeeViewTextView = ItemView.findViewById<TextView>(R.id.ReasonEmployeeViewTextView)
        val deleteLeaveButton = ItemView.findViewById<ImageButton>(R.id.deleteLeaveButton)
    }


    // binds the list items (data) to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFrom = leaveRecord[position].startDate
        val dateTo = leaveRecord[position].endDate
        val status = leaveRecord[position].status
        val leaveType = leaveRecord[position].leaveType
        val reason = leaveRecord[position].reason
        val lid = leaveRecord[position].lid
        holder.leaveDateFromTextView.text = dateFrom
        holder.leaveDateToTextView.text = dateTo
        holder.leaveStatusTextView.text = status
        holder.leaveTypeEmployeeViewTextView.text = leaveType
        holder.ReasonEmployeeViewTextView.text = reason
        //logic to check the status of the leave and apply background color depending on the status
        when(holder.leaveStatusTextView.text) {
            ClaimLeaveStatusEnum.APPROVED.status -> holder.leaveStatusTextView.setBackgroundColor(
                Color.GREEN)
            ClaimLeaveStatusEnum.REJECTED.status -> holder.leaveStatusTextView.setBackgroundColor(
                Color.RED)
            ClaimLeaveStatusEnum.PENDING.status -> holder.leaveStatusTextView.setBackgroundColor(
                Color.YELLOW)
        }

        //delete button which calls the deleteleave function from the activity to delete the selected leaveRecord
        holder.deleteLeaveButton.setOnClickListener {
            if(context is EmployeeDeleteLeaveActivity ){
                context.deleteLeave(lid)
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return leaveRecord.size
    }
}