package com.example.ict2105project.utilities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import com.example.ict2105project.R
import com.example.ict2105project.databinding.ActivityCameraPreviewBinding

/** Start Activity intent called from Profile Fragment
 *
 * A camera preview utility to access camera function to take picture
 * (This activity can be called from any fragments/activity)
 *
 * Available functions/features:
 *      Take or Retake image
 *      Delete image taken
 *      Preview taken image before change confirmation
 *      Confirm changes to profile image with the latest image taken
 *      Abort changes (no taken image will be saved)
 *
 * Set RESULT_OK and set bitmap image as extras to allow access from other activity/fragment
 * after user confirm the changes (click the tick button)
 */
class CameraPreviewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "onCreate")

        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Disable tick button if no image is taken
        binding.confirmImageButton.isEnabled = false

        //  User cancelled the operation
        binding.cancelImageButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        //  Launch the camera function
        takePicturePreview.launch()

        //  Allow user to use the button when user click on take picture image button
        binding.cameraPreviewImageButton.isEnabled = true
        binding.cameraPreviewImageButton.setOnClickListener {
            takePicturePreview.launch()
        }

    }

    /**
     * Enable use of option menu override layout with camera_preview_menu
     * @param menu a menu for menu inflation
     * @return Boolean for successful menu inflation
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.camera_preview_menu, menu)
        return true
    }

    /**
     * Set button actions
     * (retake image button and delete image button)
     * @param item a menu item inside Menu
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionRetakeButton -> {
            takePicturePreview.launch()
            true
        }

        R.id.actionDeleteButton -> {
            binding.cameraPreviewImageButton.setImageResource(R.drawable.ic_baseline_photo_camera_100)
            binding.cameraPreviewImageButton.isEnabled = true
            binding.confirmImageButton.isEnabled = false
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Get the image take in bitmap (for any image taken)
     * Recreate the taken image in square shape
     *
     * Set RESULT_OK and the squared image as extras
     */
    private val takePicturePreview = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview(), activityResultRegistry) { bitmap: Bitmap? ->

        if (bitmap != null) {

            var photo: Bitmap? = bitmap
            photo = Bitmap.createBitmap(photo!!,0,0,bitmap.width, bitmap.width)

            binding.cameraPreviewImageButton.setImageBitmap(photo)
            binding.cameraPreviewImageButton.isEnabled = false

            binding.confirmImageButton.isEnabled = true
            binding.confirmImageButton.setOnClickListener {
                setResult(RESULT_OK, intent.putExtra("image", photo))
                // finish this activity and return to previous page
                finish()
            }

        }

    }

    companion object {
        /* constant variable */
        private const val TAG = "CameraPreviewActivity"
    }

}