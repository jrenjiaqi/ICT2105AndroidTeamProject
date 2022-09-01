package com.example.ict2105project.leaves

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.entity.LeaveRecord
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.viewmodel.LeaveViewModel
import com.example.ict2105project.utilities.InputValidator
import com.example.ict2105project.viewmodel.LeaveViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class EmployeeApplyLeaveActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var leaveType = arrayOf<String?>("Casual","Sick","Annual","Maternity","ChildCare")
    private lateinit var sharedPref: SharedPreferences

    //create view for employee leave application
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_leave_application)

        //-----------------------------datepicker code----------------------------------------------
        val startDateButton = findViewById<ImageButton>(R.id.startDateButton)
        val endDateButton = findViewById<ImageButton>(R.id.endDateButton)
        val startDateTextInput = findViewById<TextView>(R.id.startDateTextView)
        val endDateTextInput = findViewById<TextView>(R.id.endDateTextView)
        val leaveReasonTextInput = findViewById<TextInputEditText>(R.id.leaveReasonTextInput)
        val calender = Calendar.getInstance()

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        //button for employee to select start date of the leave
        startDateButton.setOnClickListener {
            startDateTextInput.error = null
            //Creates a new date picker dialog for the current date using the parent context's default date picker dialog theme.
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, day ->
                var monthOfYear = month+1
                startDateTextInput.setText("" + day + "-" + monthOfYear + "-" + year)
            }, year, month, day)
            dpd.show()
            //set minimum date of the startDate and grey out the rest
            dpd.datePicker.minDate = calender.timeInMillis
        }

        //button for employee to select end date of the leave
        endDateButton.setOnClickListener {
            endDateTextInput.error = null
            //Creates a new date picker dialog for the current date using the parent context's default date picker dialog theme.
            val dpd = DatePickerDialog(this,  { view, year, month, day ->
                var monthOfYear = month+1
                endDateTextInput.setText("" + day + "-" + monthOfYear + "-" + year)
            }, year, month, day)
            dpd.show()

            //parse string into date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val parts: List<String> = startDateTextInput.text.split("-")
            val startDate = "" + parts[0] + "/" + parts[1] + "/" + parts[2]
            var startDate2 = dateFormat.parse(startDate)
            //set minimum date of the endDate and grey out the rest
            dpd.datePicker.minDate = startDate2.time
        }

        //-----------------------------spinner code-------------------------------------------------
        val spin = findViewById<Spinner>(R.id.leaveTypeSpinner)
        spin.onItemSelectedListener = this

        // Create the instance of ArrayAdapter which stores the leavetype
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            leaveType)

        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the Spinner which binds data to spinner
        spin.adapter = ad

        //-----------------------------alert dialog code--------------------------------------------
        val leave_confirmation_button = findViewById<Button>(R.id.leave_confirmation_button)


        //confirmation button for leave application
        leave_confirmation_button.setOnClickListener {
            //validation for leave (reason,startDate,EndDate)
            val reasonvalidatation = InputValidator.isValidLeavesReason(leaveReasonTextInput.text.toString())
            val startDatevalidatation = InputValidator.isValidDateFormat(startDateTextInput.text.toString())
            val endDatevalidatation = InputValidator.isValidDateFormat(endDateTextInput.text.toString())

            //set text to invalid if did not pass the validation
            if(!startDatevalidatation){
                startDateTextInput.setError("Start date invalid!")
            }
            else if(!endDatevalidatation){
                endDateTextInput.setError("End date invalid!")
            }
            else if(!reasonvalidatation){
                leaveReasonTextInput.setError(
                    "Reason cannot be empty or blank/more than 80 characters!")
            } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.leave_dialog_title)
            //confirmation message
            val msg =  "please check the details\n"+"Leave type: "+spin.selectedItem.toString()+
                    "\nStart Date: "+startDateTextInput.text+"\nEnd Date: "+
                    endDateTextInput.text+"\nReason: "+leaveReasonTextInput.text+""
            //set message for alert dialog
            builder.setMessage(msg)

                //perform positive action if user ensure the detail is right
                builder.setPositiveButton("Confirm"){dialogInterface, which ->
                    val view = View.inflate(this@EmployeeApplyLeaveActivity, R.layout.activity_leave_alert, null)
                    val builder = AlertDialog.Builder(this@EmployeeApplyLeaveActivity)
                    builder.setView(view)

                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setCancelable(false)

                    //when user press confirm, insert data into database
                    view.findViewById<Button>(R.id.leave_confirm2_button).setOnClickListener {
                        val viewModel: LeaveViewModel by viewModels{
                            LeaveViewModelFactory((application as HRApp).repo)
                        }


                        val startDate:String = startDateTextInput.text.toString()
                        val endDate:String = endDateTextInput.text.toString()
                        val reason:String = leaveReasonTextInput.text.toString()
                        val leaveType = spin.selectedItem.toString()
                        //shared preference to retrieve the employee id
                        sharedPref = getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
                        val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()
                        //create a new leave record base on the user input
                        val LeaveRecord = LeaveRecord(eidSP, startDate =startDate,
                            endDate = endDate,
                            reason = reason, leaveType = leaveType ,
                            ClaimLeaveStatusEnum.PENDING.status)


                        //insert the record into db base on the employee ID
                        viewModel.insertRecord(LeaveRecord)
                        dialog.dismiss()
                        this.finish()
                    }
                }


            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                Log.d("cancel","canceled application")
//                Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }}

    }


    //-----------------------------spinner code-----------------------------------------------------
    //action to be perform when the the item is selected for spinner
    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View, position: Int,
                                id: Long) {
        Log.d("itemSelect",leaveType[position].toString())
    }
    //action to be perform when nothing is selected for spinner
    override fun onNothingSelected(parent: AdapterView<*>?) {}

}

