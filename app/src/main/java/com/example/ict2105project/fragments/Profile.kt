package com.example.ict2105project.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.databinding.FragmentProfileBinding
import com.example.ict2105project.profile.ManagerProfileAddEmployeeActivity
import com.example.ict2105project.profile.ProfileEditActivity
import com.example.ict2105project.profile.ProfileEditChangePasswordActivity
import com.example.ict2105project.utilities.CameraPreviewActivity
import com.example.ict2105project.viewmodel.MainViewModel
import com.example.ict2105project.viewmodel.MainViewModelFactory
import com.example.ict2105project.viewmodel.ProfileViewModel
import com.example.ict2105project.viewmodel.ProfileViewModelFactory
import java.io.ByteArrayOutputStream

/**
 * A simple \[Fragment] subclass.
 * Use the \[Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {

    // Declare private late init variable
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPref: SharedPreferences

    /**
     * Create the profile fragment
     * @param savedInstanceState to saved the state of the instance
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    /**
     * To inflate the fragment_profile view
     * @param inflater to inflate the view of profile fragment
     * @param container to hold a view group
     * @param savedInstanceState to saved the state of the instance
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    /**
     * Initiate actions for inflated view of profile fragment
     * @param view the inflated view of the fragment
     * @param savedInstanceState to saved the state of the instance
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)

        Log.i(TAG, "onViewCreated")

        val mainViewModel: MainViewModel by viewModels {
            MainViewModelFactory((requireActivity().application as HRApp).repo)
        }

        /**
         * Get current logged in user eid and rid
         */
        val eid: Int? = sharedPref.getString(getString(R.string.user_eid), "")?.toIntOrNull()
        val roleID: Int? = sharedPref.getString(getString(R.string.user_role), "")?.toIntOrNull()

        /**
         * Check current user role
         */
        if (roleID != null) {
            mainViewModel.getEmployeeRole(roleID)
            mainViewModel.employeeRole.observe(viewLifecycleOwner) {
                it?.let {

                    /**
                     * Set add employee button visible only for manager and admin role
                     */
                    if (it.uppercase() != MANAGER && it.uppercase() != ADMIN)
                        binding.addEmployeeImageButton.visibility = View.INVISIBLE
                    else binding.addEmployeeImageButton.visibility = View.VISIBLE

                    Log.i(TAG, "Current User Role: $it")

                    /**
                     * Display employee role
                     */
                    binding.positionTextView.text = it
                }
            }
        }

        /**
         * Get data based on current logged in user eid
         * to display in Profile Fragment page
         */
        if (eid != null) {
            mainViewModel.getEmployee(eid)
            mainViewModel.employeeData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.nameTextView.text = it.name
                    binding.phoneNumberTextView.text = it.phoneNumber
                    binding.emailTextView.text = it.email

                    /* Display if have existing image */
                    if (it.image != null && it.image.isNotEmpty()) {
                        val imageInBitmap: Bitmap =
                            BitmapFactory.decodeByteArray(it.image, 0, it.image.size)
                        binding.profileImageMotionButton.background = formatProfileImage(imageInBitmap)
                        Log.i(TAG, "Employee data: $it")
                    }
                }
                else {
                    binding.nameTextView.text = getString(R.string.profile_error_message_unable_to_retrieve_information)
                    binding.phoneNumberTextView.text = getString(R.string.profile_error_message_unable_to_retrieve_information)
                    binding.emailTextView.text = getString(R.string.profile_error_message_unable_to_retrieve_information)
                }
            }
        }

            /* Listen for any button click activity */
        /**
         * Redirect to add employee activity
         * Visible only to Manager and Admin
         */
        binding.addEmployeeImageButton.setOnClickListener{
            Log.i(TAG, "addEmployeeImageButton")
            startActivity(Intent(context, ManagerProfileAddEmployeeActivity::class.java))
        }

        /**
         * Redirect to edit profile activity
         * for updating of current logged in user profile information
         */
        binding.editProfileButton.setOnClickListener{
            Log.i(TAG, "editProfileButton")

            editProfile.launch(
                Intent(context, ProfileEditActivity::class.java)
                        .putExtra("name", binding.nameTextView.text.toString())
                        .putExtra("phoneNumber", binding.phoneNumberTextView.text.toString())
                        .putExtra("email", binding.emailTextView.text.toString())
                        .putExtra("roleDescription", binding.positionTextView.text.toString())
            )
        }

        /**
         * Redirected to Change Password activity
         * for changing password of current logged in user
         */
        binding.changePasswordButton.setOnClickListener {
            Log.i(TAG, "changePasswordButton")
            startActivity(Intent(activity, ProfileEditChangePasswordActivity::class.java))
        }

        /**
         * To request permission from user to allow camera access
         */
        binding.cameraImageButton.setOnClickListener {
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        }
    }

    /**
     * Capture user response
     *
     * Redirect to camera preview activity to launch the camera
     * when user allow the usage of camera
     */
    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ isGranted ->
        Log.i(TAG, "requestCameraPermission")

        if (isGranted) {
            cameraPreview.launch(
                Intent(context, CameraPreviewActivity::class.java)
            )
        }
        else {
            Toast.makeText(context, "Permission Denied. Unable to launch.", Toast.LENGTH_SHORT).show()
        }
    }

    /** Get result from CameraPreviewActivity
     *
     * Get bitmap image data for any image captured (RESULT_OK)
     *  '-> Set profile image after formatting (resize and make it as a circular image)
     *  '-> Convert to ByteArray data type for storing to room database
     */
    private val cameraPreview =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i(TAG, "cameraPreview")

            if (result.resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    val image: Bitmap? = result.data?.getParcelableExtra("image")

                    if (image != null) {
                        binding.profileImageMotionButton.background = formatProfileImage(image)

                        val eidSP: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()

                        /**
                         * Convert Bitmap Image to ByteArray to store into room database
                         */
                        val imageToByteArrayOutputStream = ByteArrayOutputStream()
                        image.compress(Bitmap.CompressFormat.JPEG, 100, imageToByteArrayOutputStream)

                        val imageInByteArray: ByteArray = imageToByteArrayOutputStream.toByteArray()

                        val profileViewModel: ProfileViewModel by viewModels{
                            ProfileViewModelFactory((requireActivity().application as HRApp).repo)
                        }

                        profileViewModel.updateEmployeeImage(eidSP,imageInByteArray)

                        profileViewModel.isImageUpdated.observe(this) {
                            if (it)
                                Toast.makeText(context,"Successfully Updated Image", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(context,"Failed to Update Image", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                        Toast.makeText(this.context, "Image failed to load", Toast.LENGTH_SHORT).show()
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation
                Toast.makeText(this.context, "Change Abort", Toast.LENGTH_SHORT).show()
            }
    }

    /** Get result from ProfileEditActivity
     *
     * Refresh fragment to reflect newly updated information (RESULT_OK)
     * when current logged in user edited their profile information
     */
    private val editProfile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i(TAG, "editProfile")

            if (result.resultCode == Activity.RESULT_OK) {
                // Find the current fragment
                val currentFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentHolder)
                // Set two separate transaction
                val detachTransaction = requireActivity().supportFragmentManager.beginTransaction()
                val attachTransaction = requireActivity().supportFragmentManager.beginTransaction()

                /**
                 * Commence refreshing of the current fragment
                 * by committing detach and attach transactions
                 */
                currentFragment?.let {
                    detachTransaction.detach(it).commit()
                    attachTransaction.attach(it).commit()
                }
            }
            else
                Toast.makeText(this.context, "Error! Failed to get updated information!", Toast.LENGTH_SHORT).show()
        }

    /**
     * Resize/Crop Profile image
     * Make round corner Profile image
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

    companion object {
        /* constant variable */
        private const val TAG = "ProfileFragment"
        private const val MANAGER = "MANAGER"
        private const val ADMIN = "ADMIN"
        private const val IMAGE_SIZE = 400
    }
}
