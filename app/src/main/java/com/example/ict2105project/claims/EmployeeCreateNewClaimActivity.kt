package com.example.ict2105project.claims

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.databinding.ActivityEmployeeCreateClaimsBinding
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.utilities.InputValidator
import com.example.ict2105project.viewmodel.ClaimsViewModel
import com.example.ict2105project.viewmodel.ClaimsViewModelFactory
import java.util.*

/**
 * Activity class for employee creating a new claims activity
 */
class EmployeeCreateNewClaimActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // for setting up the binding
    private lateinit var binding: ActivityEmployeeCreateClaimsBinding
    // for using the sharedPref to access the role ID
    private lateinit var sharedPref: SharedPreferences

    // for setting up the spinner
    private lateinit var employeeClaimsSpinnerText: String

    //setting up the claimsviewmodel for claims
    val claimsViewModel: ClaimsViewModel by viewModels{
        ClaimsViewModelFactory((application as HRApp).repo)
    }

    /**
     * Sets up all the elements in the activity
     * @param savedInstanceState bundle to save the state throughout lifecycle
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeCreateClaimsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // use sharedPref to access the employee ID, condition to decide whether to show manager items
        sharedPref = this.getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        var eid: Int = sharedPref.getString("EMPLOYEEID","")?.toInt()!!

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        binding.employeeClaimsDateButton.setOnClickListener {
            // clears any error flagged from previous input
            binding.employeeClaimsDateInputTextView.error = null
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener
            { view, year, month, day ->
                // Display Selected date in TextView
                var monthOfYear = month+1
                binding.employeeClaimsDateInputTextView.setText("" + day + "-" + monthOfYear + "-" + year)
                Toast.makeText(this, binding.employeeClaimsDateInputTextView.text, Toast.LENGTH_SHORT).show()
            }, year, month, day)
            dpd.show()
            dpd.datePicker.maxDate = calender.timeInMillis
        }

        val claimsTypeSpinner: Spinner = findViewById<Spinner>(R.id.employeeClaimsTypeSpinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.claims_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            claimsTypeSpinner.adapter = adapter
            claimsTypeSpinner.onItemSelectedListener = this
        }

        // when user clicks on this button, opens the browser and go to this image hosting website
        binding.employeeClaimsImageSiteButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://imgbb.com/"))
            this.startActivity(i)
        }

        /*
         * when users click on submit button, check the input validity, then if it passes validator,
         * build an alert window that reminds the user of the fields they have entered, then when
         * the user presses confirm, it saves the fields into the database via the viewmodel.
         */
        binding.employeeClaimsSubmitButton.setOnClickListener {

            val claimsType: String = employeeClaimsSpinnerText
            val claimsDate: String = binding.employeeClaimsDateInputTextView.text.toString()
            val claimsAmount: String = binding.employeeClaimsAmountEditText.text.toString()
            val claimsImageLink: String = binding.employeeClaimsItemImageLinkEditText.text.toString()
            val claimsReason: String = binding.employeeClaimsReasonEditText.text.toString()

            // check input validity
            val validClaimsType: Boolean = InputValidator.isValidClaimsType(claimsType)
            val validClaimsDate: Boolean = InputValidator.isValidDateFormat(claimsDate)
            val validClaimsAmount: Boolean = InputValidator.isValidClaimsAmount(claimsAmount)
            val validClaimsImageLink: Boolean = InputValidator.isValidClaimsImageLink(claimsImageLink)
            val validClaimsReason: Boolean = InputValidator.isValidClaimsReason(claimsReason)

            var invalidFieldsPresent: Boolean = false

            if (!validClaimsType || !validClaimsDate || !validClaimsAmount || !validClaimsReason || !validClaimsImageLink) {
                invalidFieldsPresent = true
            }

            // defines the error statements for particular fields which doesn't pass validation
            when {
                !validClaimsDate -> binding.employeeClaimsDateInputTextView.error =
                    "Date is invalid!"
                !validClaimsAmount -> binding.employeeClaimsAmountEditText.error =
                    "Amount is invalid! Only 0-9 or . allowed!"
                !validClaimsImageLink -> binding.employeeClaimsItemImageLinkEditText.error =
                    "Link cannot not be blank / must start with https://"
                !validClaimsReason -> binding.employeeClaimsReasonEditText.error =
                    "Reason cannot be blank / max 80 characters exceeded!"
            }

            // if all inputs are valid, go ahead and process input.
            if (!invalidFieldsPresent) {
                // all input valid, time to build alert
                val builder = android.app.AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle(R.string.employeeClaimsDialogTitle)

                val msg = "please check the details\n" + "Claims Type: " + claimsType +
                        "\nClaims Date: " + claimsDate + "\nClaims Amount: " + claimsAmount +
                        "\nImage Link: " + claimsImageLink.toString() + "\nReason: " + claimsReason + ""

                //set message for alert dialog
                builder.setMessage(msg)
                //performing positive action
                builder.setPositiveButton("Confirm") { dialogInterface, which ->
                    //                Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                    val view = View.inflate(
                        this@EmployeeCreateNewClaimActivity,
                        R.layout.activity_claims_alert, null
                    )
                    val builder =
                        android.app.AlertDialog.Builder(this@EmployeeCreateNewClaimActivity)
                    builder.setView(view)

                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setCancelable(false)

                    // inserts the fields into the database via the view model
                    val date = binding.employeeClaimsDateInputTextView.text.toString()
                    val imageURL = binding.employeeClaimsItemImageLinkEditText.text.toString()
                    val amount = binding.employeeClaimsAmountEditText.text.toString().toFloat()
                    val reason = binding.employeeClaimsReasonEditText.text.toString()
                    claimsViewModel.addOneClaimRecord(
                        eid, date, employeeClaimsSpinnerText, imageURL, amount, reason,
                        ClaimLeaveStatusEnum.PENDING.status // new claim applications will be 'pending'
                    )
                    // refreshes the list of claims to keep it up-to-date with the latest insert
                    claimsViewModel.getListOfClaimsByEidAndStatus(eid, ClaimLeaveStatusEnum.PENDING.status)

                    view.findViewById<Button>(R.id.claimsAlertConfirmButton).setOnClickListener {
                        dialog.dismiss()
                        // return to main screen
                        this.finish()
                    }
                }
                //performing cancel action
                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                    // do no additional actions except to cancel
                }

                // Create the AlertDialog
                val alertDialog: android.app.AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

        }
    }

    /**
     * Defines the action to do when an item is selected in any spinners
     * @param parent the AdapterView
     * @param view the View
     * @param pos the position of the spinner item
     * @param id the id of the spinner item
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        employeeClaimsSpinnerText = parent!!.getItemAtPosition(pos).toString()
    }

    /**
     * Defines the action to do when nothing is selected in any spinners
     * @param parent the AdapterView
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        employeeClaimsSpinnerText = R.string.claims_undefined.toString()
    }
}