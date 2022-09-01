package com.example.ict2105project.profile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.databinding.ActivityManagerProfileAddEmployeeBinding
import com.example.ict2105project.databinding.ActivityProfileConfirmationDialogBoxBinding
import com.example.ict2105project.entity.Employee
import com.example.ict2105project.utilities.InputValidator
import com.example.ict2105project.utilities.MD5Hasher
import com.example.ict2105project.viewmodel.ProfileViewModel
import com.example.ict2105project.viewmodel.ProfileViewModelFactory

/** Start Activity intent called from Profile Fragment
 *
 * An activity for Manager/Admin to add new Employee profile information
 * New Employee default password: pass
 */
class ManagerProfileAddEmployeeActivity: AppCompatActivity() {

    /**
     *  Declare private late init variable
     */
    private lateinit var binding: ActivityManagerProfileAddEmployeeBinding
    //  binding for dialog box layout
    private lateinit var dialogBoxBinding: ActivityProfileConfirmationDialogBoxBinding

    /**
     * Create the manager profile add employee activity
     * @param savedInstanceState to saved the state of the instance
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "onCreate")

        binding = ActivityManagerProfileAddEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileViewModel: ProfileViewModel by viewModels {
            ProfileViewModelFactory((application as HRApp).repo)
        }

        /**
         * Set up alert dialog box
         */
        val alertDialog: AlertDialog = prepareDialogLayout()

        /**
         * Dismiss dialog and redirect back to Profile fragment
         */
        dialogBoxBinding.confirmationCancelButton.setOnClickListener {
            Toast.makeText(applicationContext, "Change Aborted", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
            finish()
        }


        var newRoleName = ""

        /**
         * Listen for any checked radio button
         */
        binding.positionRadioGroup.setOnCheckedChangeListener { _, _ ->
            when {
                binding.adminRadioButton.isChecked ->
                    newRoleName = binding.adminRadioButton.text.toString()
                binding.managerRadioButton.isChecked ->
                    newRoleName = binding.managerRadioButton.text.toString()
                binding.staffRadioButton.isChecked ->
                    newRoleName = binding.staffRadioButton.text.toString()
            }
        }

        /**
         * Listen for add employee confirmation
         *
         * Check validity/violations: (show error for)
         *      Empty fields
         *      Did not pass the input validation
         *
         * Request updating the changes to room database when there is no violations
         */
        binding.saveChangeButton.setOnClickListener {

            // Get user input
            val newName = binding.nameEditText.text.toString()
            val newPhoneNo = binding.phoneNumberEditText.text.toString()
            val newEmail = binding.emailEditText.text.toString()

            val isFieldEmpty: Boolean = newName.isEmpty() || newRoleName.isEmpty() ||
                                        newPhoneNo.isEmpty() || newEmail.isEmpty()
            val isFieldValid: Boolean = !InputValidator.isNameValid(newName) ||
                                        !InputValidator.isPhoneNumberValid(newPhoneNo) ||
                                        !InputValidator.isLoginEmailValid(newEmail)

            clearErrorMessage()

            if (isFieldEmpty)
                setFieldEmptyMessage(newName, newRoleName,newPhoneNo,newEmail)

            else {
                if (isFieldValid)
                    setNotValidMessage(newName, newPhoneNo, newEmail)

                else {

                    profileViewModel.getAllRole()

                    profileViewModel.listOfRoles.observe(this) { list ->
                        if (list != null) {
                            for (role in list) {
                                /**
                                 * Check for role name and return rid for Employee object insertion
                                 */
                                if (newRoleName.uppercase() == role.name.uppercase()) {

                                    alertDialog.show()
                                    dialogBoxBinding.confirmationConfirmButton.setOnClickListener {

                                        profileViewModel.insertNewEmployeeInfo(
                                            Employee(newName, newPhoneNo, newEmail, MD5Hasher.getEncodedStringValue(DEFAULT_PASSWORD), role.rid)
                                        )

                                        profileViewModel.isInfoInserted.observe(this) { isInserted ->
                                            clearErrorMessage()
                                            if (isInserted) {
                                                Toast.makeText(applicationContext, "Successfully Inserted", Toast.LENGTH_SHORT).show()
                                                alertDialog.dismiss()
                                                // finish this activity and return to previous page
                                                finish()
                                            }
                                            else {
                                                profileViewModel.failedInsertMessage.observe(this) { failedMsg ->
                                                    if (failedMsg != null) {
                                                        binding.emailEditText.error = failedMsg
                                                    }
                                                    else binding.emailEditText.error = null
                                                }
                                                alertDialog.dismiss()
                                            }
                                        }
                                    }
                                    break
                                }
                                else
                                    Toast.makeText(applicationContext, "No Existing Roles", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Clear existing error message
     */
    private fun clearErrorMessage() {
        binding.nameEditText.error = null
        binding.phoneNumberEditText.error = null
        binding.emailEditText.error = null
    }

    /**
     * Set up Dialog Layout design
     *
     * @return AlertDialog a subclass of Dialog containing a dialog box with button, text, image etc.
     */
    private fun prepareDialogLayout(): AlertDialog {
        dialogBoxBinding = ActivityProfileConfirmationDialogBoxBinding.inflate(layoutInflater)

        // Override existing warning header and message in dialog box layout
        dialogBoxBinding.confirmationHeaderTextView.text = getString(R.string.edit_profile_text_view_add_employee)
        dialogBoxBinding.confirmationMessageTextView.text = getString(R.string.edit_profile_text_view_add_employee_message)

        val alertDialogBuilder = AlertDialog.Builder(this).setView(dialogBoxBinding.root)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return alertDialog
    }

    /**
     * Set error message if its field is empty
     *
     * @param newName a string containing name of employee keyed in by current logged in Manager/Admin
     * @param newRoleName a string containing role of employee keyed in by current logged in Manager/Admin
     * @param newPhoneNo a string containing phone number of employee keyed in by current logged in Manager/Admin
     * @param newEmail a string containing email of employee keyed in by current logged in Manager/Admin
     */
    private fun setFieldEmptyMessage(newName: String, newRoleName: String, newPhoneNo: String, newEmail: String) {
        if (newName.isEmpty()) binding.nameEditText.error = "Name should not be empty!"
        if (newRoleName.isEmpty())
            Toast.makeText(applicationContext, "Please assign a position!", Toast.LENGTH_SHORT).show()
        if (newPhoneNo.isEmpty()) binding.phoneNumberEditText.error = "Phone number should not be empty!"
        if (newEmail.isEmpty()) binding.emailEditText.error = "Email should not be empty!"
    }

    /**
     * Set error message if user input did not pass the validation checks
     *
     * @param newName a string containing user keyed in employee name
     * @param newPhoneNo a string containing user keyed in employee phone number
     * @param newEmail a string containing user keyed in employee email
     */
    private fun setNotValidMessage(newName: String, newPhoneNo: String, newEmail: String) {
        if (!InputValidator.isNameValid(newName)) binding.nameEditText.error =
            "$newName is not a valid name.\nFormat: Xxxxx Xxxxx"
        if (!InputValidator.isPhoneNumberValid(newPhoneNo)) binding.phoneNumberEditText.error =
            "$newPhoneNo is not a valid phone number."
        if (!InputValidator.isLoginEmailValid(newEmail)) binding.emailEditText.error =
            "$newEmail is not a valid email.\n\nFormat:\n\t\tx@x.com\n\t\tx@x.sg\n\t\tx@x.x.com\n\t\tx@x.x.sg"
    }

    companion object {
        /* constant variable */
        private const val TAG = "AddEmployeeActivity"
        private const val DEFAULT_PASSWORD = "pass"
    }
}