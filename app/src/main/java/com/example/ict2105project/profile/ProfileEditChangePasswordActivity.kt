package com.example.ict2105project.profile

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.databinding.ActivityProfileConfirmationDialogBoxBinding
import com.example.ict2105project.databinding.ActivityProfileEditChangePasswordBinding
import com.example.ict2105project.utilities.MD5Hasher
import com.example.ict2105project.viewmodel.ProfileViewModel
import com.example.ict2105project.viewmodel.ProfileViewModelFactory

/** Start Activity intent called from Profile Fragment
 *
 * An activity for current logged in user
 * to change their password
 */
class ProfileEditChangePasswordActivity: AppCompatActivity() {

    /**
     *  Declare private late init variable
     */
    private lateinit var binding: ActivityProfileEditChangePasswordBinding
    //  binding for dialog box layout
    private lateinit var dialogBoxBinding: ActivityProfileConfirmationDialogBoxBinding
    private lateinit var sharedPref: SharedPreferences

    /**
     * Create the profile edit change password activity
     * @param savedInstanceState to saved the state of the instance
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileEditChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)

        val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()

        val profileViewModel: ProfileViewModel by viewModels {
            ProfileViewModelFactory((application as HRApp).repo)
        }

        /**
         * Set up alert dialog box
         */
        val alertDialog: AlertDialog = prepareDialogLayout()

        /**
         * Listen for any cancel operation from dialog box
         */
        dialogBoxBinding.confirmationCancelButton.setOnClickListener {
            Toast.makeText(applicationContext,"Change Aborted", Toast.LENGTH_SHORT).show()
            // dismiss pop-up dialog box
            alertDialog.dismiss()
            // finish this activity and return to previous page
            finish()
        }

        /**
         * Listen for password change confirmation
         *
         * Check validity/violations: (show error for)
         *      Empty fields
         *      Wrong password
         *      Existing password
         *      New password and Confirmation password does not match
         *
         * All input password were hashed when there is no empty field
         *
         * Request updating the changes to room database (hashed password)
         * when there is no violations
         */
        binding.saveChangeButton.setOnClickListener {
            val currentPass = binding.currentPasswordEditText.text.toString()
            val newPass = binding.newPasswordEditText.text.toString()
            val confirmPass = binding.confirmPasswordEditText.text.toString()

            val isFieldEmpty: Boolean = currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()

            if (isFieldEmpty)
                setFieldEmptyMessage(currentPass,newPass,confirmPass)

            else {
                /** HASHED Password
                 * existingPass - password user used to logged in to this application
                 * currentPassHashed - password user keyed in (should match existingPass)
                 * newPassHashed - new password user want to change to
                 * confirmPassHashed - new password user retype (should match newPassHashed)
                 */
                val currentPassHashed = MD5Hasher.getEncodedStringValue(currentPass)
                val newPassHashed = MD5Hasher.getEncodedStringValue(newPass)
                val confirmPassHashed = MD5Hasher.getEncodedStringValue(confirmPass)

                profileViewModel.getEmployeePassword(eidSP)

                profileViewModel.employeePassword.observe(this) { existingPass ->

                    if (existingPass != null) {
                        if (existingPass == currentPassHashed) {

                            clearErrorMessage()
                            when {
                                existingPass == newPassHashed ->
                                    binding.newPasswordEditText.error = "Password exist."

                                newPassHashed == confirmPassHashed -> {
                                    // pop up dialog to double check whether user want to change password
                                    alertDialog.show()

                                    dialogBoxBinding.confirmationConfirmButton.setOnClickListener {

                                        profileViewModel.updateEmployeePassword(eidSP, newPassHashed)

                                        profileViewModel.isPasswordUpdated.observe(this) { isUpdated ->
                                            if (isUpdated)
                                                Toast.makeText(applicationContext,
                                                    "Successfully Updated",
                                                    Toast.LENGTH_SHORT).show()
                                            else
                                                Toast.makeText(applicationContext,
                                                    "Failed to Updated",
                                                    Toast.LENGTH_SHORT).show()
                                        }
                                        alertDialog.dismiss()
                                        // finish this activity and return to previous page
                                        finish()
                                    }
                                }

                                else -> binding.confirmPasswordEditText.error =
                                        "Password confirmation does not match."
                            }

                        } else binding.currentPasswordEditText.error = "Wrong Password."
                    } else Toast.makeText(this, "No existing password!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Clear existing error message
     */
    private fun clearErrorMessage() {
        binding.currentPasswordEditText.error = null
        binding.newPasswordEditText.error = null
        binding.confirmPasswordEditText.error = null
    }

    /**
     * Set up Dialog Layout design
     *
     * @return AlertDialog a subclass of Dialog containing a dialog box with button, text, image etc.
     */
    private fun prepareDialogLayout(): AlertDialog {
        dialogBoxBinding = ActivityProfileConfirmationDialogBoxBinding.inflate(layoutInflater)

        val alertDialogBuilder = AlertDialog.Builder(this).setView(dialogBoxBinding.root)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return alertDialog
    }

    /**
     * Set error message if its field is empty
     *
     * @param currentPass a string containing user keyed in password of current logged in employee
     * @param newPass a string containing user keyed in new password of current logged in employee
     * @param confirmPass a string containing user keyed in confirm password of current logged in employee
     */
    private fun setFieldEmptyMessage(currentPass: String, newPass: String, confirmPass: String) {
        if (currentPass.isEmpty()) binding.currentPasswordEditText.error = "This field should not be empty!"
        if (newPass.isEmpty()) binding.newPasswordEditText.error = "This field should not be empty!"
        if (confirmPass.isEmpty()) binding.confirmPasswordEditText.error = "This field should not be empty!"
    }

}