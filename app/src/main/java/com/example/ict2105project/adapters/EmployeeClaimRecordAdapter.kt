package com.example.ict2105project.adapters

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.R
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.enum.ClaimLeaveStatusEnum

/**
 * Adapter for employee claim record recyclerview.
 * @param itemList List of Claim Records.
 */
class EmployeeClaimRecordAdapter(itemList: List<ClaimRecord>)
    : RecyclerView.Adapter<EmployeeClaimRecordAdapter.ViewHolder>() {

    // a local variable that stores the list of claim records
    var claimRecordList = itemList

    /**
     * Inflates layout for the XML to occupy the screen.
     * @return instance of view holder object.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeClaimRecordAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                              .inflate(R.layout.item_layout_employee_claims, parent, false)
        return ViewHolder(v)
    }

    /**
     * Populates the items from the claims list into the view holder object by their position.
     * @param holder the view holder.
     * @param position the position of the recyclerview item.
     */
    override fun onBindViewHolder(holder: EmployeeClaimRecordAdapter.ViewHolder, position: Int) {
        // display the data from claimRecordList
        holder.eid.text = claimRecordList[position].eid.toString()
        holder.cid.text = claimRecordList[position].cid.toString()
        holder.date.text = claimRecordList[position].date.toString()
        holder.type.text = claimRecordList[position].title.toString()
        holder.amount.text = "$"+(String.format("%.2f", claimRecordList[position].amount))
        holder.reason.text = claimRecordList[position].reason.toString()
        holder.status.text = claimRecordList[position].status.toString()
        when(holder.status.text) {
            ClaimLeaveStatusEnum.APPROVED.status -> holder.status.setBackgroundColor(Color.GREEN)
            ClaimLeaveStatusEnum.REJECTED.status -> holder.status.setBackgroundColor(Color.RED)
            ClaimLeaveStatusEnum.PENDING.status -> holder.status.setBackgroundColor(Color.YELLOW)
        }
        holder.imageLinkBtn.setOnClickListener { v ->
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(claimRecordList[position].image_url))
            v.context.startActivity(i)
        }
        // create alternate colors for cardview
        if (position % 2 == 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#F9F9F9"))
        }
    }

    /**
     * gets the number of items in the list.
     * @return number of items in the list.
     */
    override fun getItemCount(): Int {
        return claimRecordList.size
    }

    /**
     * Defines a view holder for the recyclerview.
     * @param itemView view of the item inside the recyclerview.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cid: TextView
        var eid: TextView
        var date: TextView
        var type: TextView
        var amount: TextView
        var reason: TextView
        var status: TextView
        var imageLinkBtn: ImageButton
        var cardView: CardView

        init {
            cid = itemView.findViewById<TextView>(R.id.employeeClaimsItemCIDTextView)
            eid = itemView.findViewById<TextView>(R.id.employeeClaimsItemEIDTextView)
            date = itemView.findViewById<TextView>(R.id.employeeClaimsItemDateTextView)
            type = itemView.findViewById<TextView>(R.id.employeeClaimsItemTypeTextView)
            amount = itemView.findViewById<TextView>(R.id.employeeClaimsItemAmountTextView)
            status = itemView.findViewById<TextView>(R.id.employeeClaimItemStatusTextView)
            reason = itemView.findViewById<TextView>(R.id.employeeClaimsItemReasonTextView)
            imageLinkBtn = itemView.findViewById<ImageButton>(R.id.employeeClaimsItemImageLinkButton)
            cardView = itemView.findViewById<CardView>(R.id.employeeClaimsItemCardView)
        }
    }
}