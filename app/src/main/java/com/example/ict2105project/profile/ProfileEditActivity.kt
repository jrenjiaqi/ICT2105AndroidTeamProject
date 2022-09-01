package com.example.ict2105project.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.databinding.ActivityProfileEditBinding
import com.example.ict2105project.utilities.InputValidator
import com.example.ict2105project.viewmodel.ProfileViewModel
import com.example.ict2105project.viewmodel.ProfileViewModelFactory

/** Start Activity intent called from Profile Fragment
 *
 * Extras passed from intent: (key)
 *      name, phoneNumber, email, roleDescription
 *
 * An activity for current logged in user
 * to update their profile information
 *
 * set RESULT_OK for any changes updated to the room database
 */
class ProfileEditActivity : AppCompatActivity() {

    // Declare private late init variable
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var sharedPref: SharedPreferences

    /**
     * Create the profile edit activity
     * @param savedInstanceState to saved the state of the instance
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "onCreate")

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileViewModel: ProfileViewModel by viewModels{
            ProfileViewModelFactory((application as HRApp).repo)
        }

        /**
         * Display existing Profile data
         * intent data get from Profile fragment
         */
        sharedPref = getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        val intent: Intent? = intent

        val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()
        val name: String? = intent?.getStringExtra("name")
        val phoneNo: String? = intent?.getStringExtra("phoneNumber")
        val email: String? = intent?.getStringExtra("email")
        val position: String? = intent?.getStringExtra("roleDescription")

        if (name != null)
            binding.nameEditText.setText(name)
        else binding.nameEditText.error = "Could not get your details."

        if (phoneNo != null)
            binding.phoneNumberEditText.setText(phoneNo)
        else binding.phoneNumberEditText.error = "Could not get your details."

        if (email != null)
            binding.emailEditText.setText(email)
        else binding.emailEditText.error = "Could not get your details."

        if (position != null)
            binding.positionTextView.text = position
        else binding.positionTextView.error = "Could not get your details."

        /**
         * Get employee image from room database
         * Display if there is existing profile image
         */
        profileViewModel.getEmployeeImage(eidSP)
        profileViewModel.employeeImage.observe(this) {
            if (it != null && it.isNotEmpty()) {
                val imageInBitmap: Bitmap =
                    BitmapFactory.decodeByteArray(it, 0, it.size)
                binding.profileImageMotionButton.background = formatProfileImage(imageInBitmap)
            }
        }

        /**
         * Check validity (empty field, validation checks)
         * Request updating the changes to database though MVVM (if any)
         */
        binding.saveChangeButton.setOnClickListener {

            val newName = binding.nameEditText.text.toString()
            val newPhoneNo = binding.phoneNumberEditText.text.toString()
            val newEmail = binding.emailEditText.text.toString()

            val isFieldEmpty: Boolean = newName.isEmpty() || newPhoneNo.isEmpty() || newEmail.isEmpty()
            val isFieldChange: Boolean = !(name.equals(newName) && phoneNo.equals(newPhoneNo) &&
                                           email.equals(newEmail))
            val isFieldValid: Boolean = !InputValidator.isNameValid(newName) ||
                                        !InputValidator.isPhoneNumberValid(newPhoneNo) ||
                                        !InputValidator.isLoginEmailValid(newEmail)

            // clear any existing error message
            clearErrorMessage()

            if (isFieldEmpty)
                setFieldEmptyMessage(newName,newPhoneNo,newEmail)

            else {
                when {
                    !isFieldChange ->
                        Toast.makeText(applicationContext, "No Changes Made", Toast.LENGTH_SHORT).show()

                    isFieldValid -> setNotValidMessage(newName,newPhoneNo,newEmail)

                    else -> {
                        profileViewModel.updateEmployeeById(eidSP,newName,newPhoneNo,newEmail)

                        profileViewModel.isInfoUpdated.observe(this) {
                            if (it) {
                                Toast.makeText(applicationContext,"Successfully Updated",Toast.LENGTH_SHORT).show()
                            }

                        }
                        setResult(RESULT_OK)
                        // finish this activity and return to previous page
                        finish()
                    }
                }
            }
        }
    }

    /**
     * Format employee image size and set it to a circular image
     *
     * @param image a bitmap image containing the employee profile image
     * @return Drawable containing RoundedBitmapDrawable of employee profile image
     */
    private fun formatProfileImage(image: Bitmap): Drawable {
        Log.i(TAG, "formatProfileImage")
        val resizeImage = Bitmap.createScaledBitmap(image, IMAGE_SIZE, IMAGE_SIZE, true)
        val roundImage = RoundedBitmapDrawableFactory.create(resources, resizeImage)
        roundImage.isCircular = true

        return roundImage
    }

    /**
     * Clear existing error message
     */
    private fun clearErrorMessage() {
        Log.i(TAG, "clearErrorMessage")
        binding.nameEditText.error = null
        binding.phoneNumberEditText.error = null
        binding.emailEditText.error = null
    }

    /**
     * Set error message if its field is empty
     *
     * @param name a string containing name of employee
     * @param phoneNo a string containing phone number of employee
     * @param email a string containing email of employee
     */
    private fun setFieldEmptyMessage(name: String, phoneNo: String, email: String) {
        Log.i(TAG, "setFieldEmptyMessage")
        if (name.isEmpty()) binding.nameEditText.error = "Name should not be empty!"
        if (phoneNo.isEmpty()) binding.phoneNumberEditText.error = "Phone number should not be empty!"
        if (email.isEmpty()) binding.emailEditText.error = "Email should not be empty"
    }

    /**
     * Set error message if user input did not pass the validation checks
     *
     * @param newName a string containing user keyed in employee name
     * @param newPhoneNo a string containing user keyed in employee phone number
     * @param newEmail a string containing user keyed in employee email
     */
    private fun setNotValidMessage(newName: String, newPhoneNo: String, newEmail: String) {
        Log.i(TAG, "setNotValidMessage")
        if (!InputValidator.isNameValid(newName)) binding.nameEditText.error =
            "$newName is not a valid name.\nFormat: Xxxxx Xxxxx"
        if (!InputValidator.isPhoneNumberValid(newPhoneNo)) binding.phoneNumberEditText.error =
            "$newPhoneNo is not a valid phone number."
        if (!InputValidator.isLoginEmailValid(newEmail)) binding.emailEditText.error =
            "$newEmail is not a valid email.\n\nFormat:\n\t\tx@x.com\n\t\tx@x.sg\n\t\tx@x.x.com\n\t\tx@x.x.sg"
    }

    companion object {
        /* constant variable */
        private const val TAG = "ProfileEditActivity"
        private const val IMAGE_SIZE = 400
    }
}