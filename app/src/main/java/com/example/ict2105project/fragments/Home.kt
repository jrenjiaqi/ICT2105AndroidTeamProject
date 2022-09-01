package com.example.ict2105project.fragments

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProviders
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.ict2105project.HRApp
import com.example.ict2105project.R
import com.example.ict2105project.service.LocationService
import com.example.ict2105project.viewmodel.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    private lateinit var currentUserEid: String

    //setting up the view model for logging out
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory((requireActivity().application as HRApp).repo)
    }
    //setting up the view model for saving checkin data for database
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as HRApp).repo)

    }

    /**
     * Creates the view for the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * After the creation of the view for the fragment
     * @param view that was created
     * @param savedInstanceState
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val logOutButton = requireView().findViewById<Button>(R.id.logOutButton)
        logOutButton.setOnClickListener {
            loginViewModel.logOutEmployee()
            //Simple workaround for the library button issue
            HomeViewModel.isErr = null
            HomeViewModel.isIn = null

            activity?.finish()
        }
        val clockOutButton = requireView().findViewById<CircularProgressButton>(R.id.clockOutButton)
        clockOutButton.setOnClickListener {
            //getting current date time
            val c = Calendar.getInstance()
            val dd = String.format("%02d", c.get(Calendar.DAY_OF_MONTH))
            val MM = String.format("%02d", c.get(Calendar.MONTH)+1)
            val yyyy = c.get(Calendar.YEAR).toString()
            val HH = String.format("%02d", c.get(Calendar.HOUR_OF_DAY))
            val mm = String.format("%02d", c.get(Calendar.MINUTE))
            val ss = String.format("%02d", c.get(Calendar.SECOND))

            //getting current user eid
            val sp = requireActivity().getSharedPreferences(
                getString(R.string.user_sharedprefkey),
                Context.MODE_PRIVATE
            )
            (sp.getString(getString(R.string.user_eid), null))?.also {
                currentUserEid = it
            }

            //formatting current date time
            val currentDateTime = dateTimeFormatter(dd, MM, yyyy, HH, mm, ss)
            homeViewModel.clockOutEmployee(currentUserEid.toInt(), currentDateTime)

            homeViewModel.isClockOut.observe(requireActivity()){
                if(it){
                    Toast.makeText(activity, "Successfully clocked out", Toast.LENGTH_LONG).show()
                    clockOutButton.visibility = View.GONE
                    val clockInButton = requireActivity().findViewById<CircularProgressButton>(R.id.clockInButton)
                    clockInButton.setBackgroundResource(R.drawable.login_button)
                    clockInButton.text = "Check In"
                } else{
                    Toast.makeText(activity, "clocking out failed", Toast.LENGTH_LONG).show()
                }
            }
        }

        /**
         * setting button for GPS clockin
         *
         * this manages the permission if the user has not granted it
         * so as to be able to start the location service for the GPS to be used
         * for clocking in
         */
        val clockInButton = requireView().findViewById<CircularProgressButton>(R.id.clockInButton)
        clockInButton.setOnClickListener {
            //https://www.tutorialspoint.com/how-to-request-location-permission-at-runtime-on-kotlin
            //requesting for permission
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
            } else { //if permission granted
                startLocationService()
                clockInButton.startAnimation()
            }
        }

        //Simple workaround for the button library issue
        if(HomeViewModel.isIn != null && HomeViewModel.isIn == true){
            clockInButton.text = "Checked In"
        }
        else if(HomeViewModel.isErr != null && HomeViewModel.isErr == true){
            clockInButton.text = "Error"
        }

        homeViewModel.isAtCompany.observe(requireActivity()) {
            if (it){ //inSingapore
                homeViewModel.isClockedIn.observe(requireActivity()) {
                    if (it) { //isCheckedIn
                        homeViewModel.isLate.observe(requireActivity()) {
                            if (it) {//isLate
                                clockInButton.setBackgroundResource(R.drawable.checked_in_button_late)
                                clockInButton.isEnabled = false
                                clockInButton.revertAnimation{
                                    clockInButton.text = "Checked in"
                                }
                            } else {//!isLate
                                clockInButton.setBackgroundResource(R.drawable.checked_in_button)
                                clockInButton.isEnabled = false
                                clockInButton.revertAnimation{
                                    clockInButton.text = "Checked in"
                                }
                            }
                        }
                        clockOutButton.visibility = View.VISIBLE
                    }
                }
            } else{ //!inSingapore
                clockInButton.setBackgroundResource(R.drawable.checked_in_button_error)
                clockInButton.isEnabled = false
                clockInButton.revertAnimation{
                    clockInButton.text = "Error"
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if((ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
                        startLocationService()
                    }
                }
                else{
                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Starts the location service
     */
    private fun startLocationService(){
        val intent = Intent(requireActivity(), LocationService::class.java)
        context?.startService(intent)
    }

    /**
     * Ends the location service
     */
    private fun endLocationService(){
        val intent = Intent(requireActivity(), LocationService::class.java)
        context?.stopService(intent)
    }

    /**
     * broadcast receiver to receive checkin result from LocationService
     *
     * it will start after receiving a broadcast from the service
     */
    private inner class LocationBroadcastReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(contenxt: Context?, intent: Intent?) {
            val bundle = intent?.extras
            val data = bundle?.getString("location_key")
            if (data != null) {
                Log.d("LOCATION BC RECEIVED", data)
                //Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()

                /**
                 * 1. getting the DateTime + required information
                 * 2. kill GPS service
                 * 4. inserting required fields through the viewmodel
                 *
                 */
                //getting current date time
                //https://stackoverflow.com/questions/70195996/how-to-get-current-time-from-device-in-kotlin
                val c = Calendar.getInstance()
                val dd = String.format("%02d", c.get(Calendar.DAY_OF_MONTH))
                val MM = String.format("%02d", c.get(Calendar.MONTH) + 1)
                val yyyy = c.get(Calendar.YEAR).toString()
                val HH = String.format("%02d", c.get(Calendar.HOUR_OF_DAY))
                val mm = String.format("%02d", c.get(Calendar.MINUTE))
                val ss = String.format("%02d", c.get(Calendar.SECOND))

                val sp = requireActivity().getSharedPreferences(
                    getString(R.string.user_sharedprefkey),
                    Context.MODE_PRIVATE
                )
                (sp.getString(getString(R.string.user_eid), null))?.also {
                    currentUserEid = it
                }

                //formatting the currentdatetime
                val currentDateTime = dateTimeFormatter(dd, MM, yyyy, HH, mm, ss)
                Log.d("CURRENT DATETIME", currentDateTime)

                Toast.makeText(requireActivity(), data.toString(), Toast.LENGTH_LONG).show()

                endLocationService()

                //clocking the user in
                homeViewModel.clockInEmployee(currentUserEid.toInt(), currentDateTime, "-", data)
            }
        }
    }

    /**
     * To register the receiver so as to be able to receive broadcasts
     */
    private val receiver: LocationBroadcastReceiver = LocationBroadcastReceiver()
    override fun onResume() {
        super.onResume()

        //to register broadcast
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.ict2105project")
        requireActivity().registerReceiver(receiver, intentFilter)
    }

    /*
     * To unregister the receiver during onpause
     */
    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(receiver)
    }

    /**
     * returns Date Time formatted in string
     *
     * @param dd Day
     * @param MM Month
     * @param yyyy Year
     * @param HH Hour (24hour format)
     * @param mm Minute
     * @param ss Second
     */
    private fun dateTimeFormatter(dd: String, MM: String, yyyy: String,
                                HH: String, mm: String, ss: String): String{
        return "$dd-$MM-$yyyy $HH:$mm:$ss"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}