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
import com.example.ict2105project.entity.ClaimRecordWithName
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Adapter for manager claim record recyclerview.
 * @param claimRecordWithNameList List of Claim Records with employee names.
 * @param approveList List of claim ids of approved Claim Records
 * @param rejectedList List of claim ids of rejected Claim Records
 */
class ManagerClaimRecordAdapter(private val claimRecordWithNameList: List<ClaimRecordWithName>,
                                private val approveList: MutableList<Int>,
                                private val rejectList: MutableList<Int>)
    : RecyclerView.Adapter<ManagerClaimRecordAdapter.ViewHolder>() {

    /**
     * Inflates layout for the XML to occupy the screen.
     * @return instance of view holder object.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManagerClaimRecordAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_manager_manage_claims, parent, false)
        return ViewHolder(v)
    }

    /**
     * Populates the items from the claims list into the view holder object by their position.
     * @param holder the view holder.
     * @param position the position of the recyclerview item.
     */
    override fun onBindViewHolder(holder: ManagerClaimRecordAdapter.ViewHolder, position: Int) {
        // display the data from claimRecordList
        holder.cid.text = claimRecordWithNameList[position].claimRecord.cid.toString()
        holder.eid.text = claimRecordWithNameList[position].claimRecord.eid.toString()
        holder.name.text = claimRecordWithNameList[position].ename.toString()
        holder.date.text = claimRecordWithNameList[position].claimRecord.date.toString()
        holder.type.text = claimRecordWithNameList[position].claimRecord.title.toString()

        // formats the string into 2dp
        val df = DecimalFormat("#.##")
        // round it down
        df.roundingMode = RoundingMode.DOWN
        // round to 2 decimal places
        holder.amount.text = "$"+(String.format("%.2f", claimRecordWithNameList[position].claimRecord.amount))
        holder.reason.text = claimRecordWithNameList[position].claimRecord.reason.toString()

        // start the browser with the URL found inside the claimRecord
        holder.imageLinkBtn.setOnClickListener { v ->
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(claimRecordWithNameList[position].claimRecord.image_url))
            v.context.startActivity(i)
        }

        // onClick for the approve button
        holder.approveBtn.setOnClickListener {
            val cid = claimRecordWithNameList[position].claimRecord.cid
            // append to approve list only if not already in either lists
            if ((cid !in approveList) && (cid !in rejectList)) {
                // if we press approve but claim item is neither in approve nor reject list
                holder.rejectBtn.setImageResource(R.drawable.ic_grey_button)
                approveList.add(cid)
            } else if ((cid in approveList) && (cid !in rejectList)) {
                // if we press reject and it is already approved (ie. retract the approval)
                holder.rejectBtn.setImageResource(R.drawable.ic_reject_leave)
                holder.rejectBtn.setImageResource(R.drawable.ic_baseline_clear_24)
                approveList.remove(cid)
            } else if ((cid !in approveList) && (cid in rejectList)) {
                // do nothing
            } else {
                // throw an exception because any other condition would be programming oversight
                throw Exception("Approve and reject claims invalid element!")
            }
        }
        holder.rejectBtn.setOnClickListener {
            val cid = claimRecordWithNameList[position].claimRecord.cid
            // append to approve list only if not already in either lists
            if ((cid !in rejectList) && (cid !in approveList)) {
                // if we press reject and the claim item is in neither in approved or rejected
                holder.approveBtn.setImageResource(R.drawable.ic_grey_button)
                rejectList.add(cid)
            } else if ((cid in rejectList) && (cid !in approveList)) {
                // if we press reject and it's already rejected (ie. want to negate rejection)
                holder.approveBtn.setColorFilter(null)
                holder.approveBtn.setImageResource(R.drawable.ic_approve_leave)
                holder.approveBtn.setImageResource(R.drawable.ic_check_leave)
                rejectList.remove(cid)
            } else if ((cid !in rejectList) && (cid in approveList)) {
                // do nothing
            } else {
                // throw an exception because any other condition would be programming oversight
                throw Exception("Approve and reject claims invalid element!")
            }
        }
        // cardview should alternate color between adjacent cards
        if (position % 2 == 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#F9F9F9"))
        }
    }

    /**
     * gets the number of items in the list.
     * @return number of items in the list.
     */
    override fun getItemCount(): Int {
        return claimRecordWithNameList.size
    }

    /**
     * Defines a view holder for the recyclerview.
     * @param itemView view of the item inside the recyclerview.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cid: TextView
        var eid: TextView
        var name: TextView
        var date: TextView
        var type: TextView
        var reason: TextView
        var amount: TextView
        var imageLinkBtn: ImageButton
        var approveBtn: ImageButton
        var rejectBtn: ImageButton
        var cardView: CardView

        init {
            cid = itemView.findViewById<TextView>(R.id.managerClaimsItemCIDTextView)
            eid = itemView.findViewById<TextView>(R.id.managerClaimsItemEIDTextView)
            name = itemView.findViewById<TextView>(R.id.managerClaimsItemEmpNameTextView)
            date = itemView.findViewById<TextView>(R.id.managerClaimsItemDateTextView)
            type = itemView.findViewById<TextView>(R.id.managerClaimsItemTitleTextView)
            reason = itemView.findViewById<TextView>(R.id.managerClaimsItemReasonTextView)
            amount = itemView.findViewById<TextView>(R.id.managerClaimsItemAmountTextView)
            imageLinkBtn = itemView.findViewById<ImageButton>(R.id.managerClaimsItemImageLinkButton)
            approveBtn = itemView.findViewById<ImageButton>(R.id.managerClaimsItemApproveButton)
            rejectBtn = itemView.findViewById<ImageButton>(R.id.managerClaimsItemRejectButton)
            cardView = itemView.findViewById<CardView>(R.id.managerClaimsItemCardView)
        }
    }
}