package com.example.ict2105project.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ict2105project.R
import android.content.SharedPreferences
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ict2105project.HRApp
import com.example.ict2105project.adapters.EmployeeAttendanceAdapter
import com.example.ict2105project.adapters.ManagerAttendanceAdapter
import com.example.ict2105project.entity.AttendanceRecord
import com.example.ict2105project.entity.AttendanceWithName
import com.example.ict2105project.utilities.InputValidator
import com.example.ict2105project.viewmodel.MainViewModel
import com.example.ict2105project.viewmodel.MainViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [Attendance.newInstance] factory method to
 * create an instance of this fragment.
 */
class Attendance : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterEmployee: RecyclerView.Adapter<EmployeeAttendanceAdapter.ViewHolder>? = null
    private var adapterManager: RecyclerView.Adapter<ManagerAttendanceAdapter.ViewHolder>? = null

    val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((requireActivity().application as HRApp).repo)
    }

    /**
     * to create the attendance fragment
     * @param savedInstanceState to save the state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * to inflate the fragment attendance view based on user role
     * @param inflater a layout inflater
     * @param conatiner a view group
     * @param savedInstanceState to save the state
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)
        val userRole: Int = sharedPref.getString(getString(R.string.user_role), "")!!.toInt()
        //check the user role to display respective layout to user
        if (userRole == 3){
            // Inflate the employee layout for this fragment
            return inflater.inflate(R.layout.activity_employee_attendance, container, false)
        }else{
            // Inflate the manager layout for this fragment
            return inflater.inflate(R.layout.activity_manager_attendance, container, false)
        }
    }

    /**
     * to initiate action on attendance fragment
     * @param view a View
     * @param savedInstanceState to save the state
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.user_sharedprefkey), Context.MODE_PRIVATE)

        super.onViewCreated(view, savedInstanceState)

        val userRole: Int = sharedPref.getString(getString(R.string.user_role), "")!!.toInt()
        val eid: Int = sharedPref.getString(getString(R.string.user_eid), "")!!.toInt()

        val btnAddAttendance = view.findViewById<Button>(R.id.addAttendanceButton)

        //prepare the layout manager
        layoutManager = LinearLayoutManager(this.context)

        //attach respective adapter to the recycler view
        if (userRole == 3){

            //setting the recyclerview container to this employee layout
            val employeeAttendanceRecyclerView = requireView().findViewById<RecyclerView>(R.id.employeeAttendanceRecyclerView)
            employeeAttendanceRecyclerView.layoutManager = layoutManager

            //get attendance records by eid
            mainViewModel.getListOfAttendanceRecordsByEid(eid)
            //observe the change in the live data and also to get the data
            mainViewModel.listOfAttendanceRecordByEid.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    requireView().findViewById<TextView>(R.id.employeeAttendnaceNoItemsTextView).visibility = View.VISIBLE
                    employeeAttendanceRecyclerView.visibility = View.GONE
                }
                // reversed so most recent attendance records show up on top of recyclerview.
                val reversedList: List<AttendanceRecord> = it.reversed()
                //initialize the adapter
                adapterEmployee = this.context?.let { it1 -> EmployeeAttendanceAdapter(it1, reversedList) }

                //setting recyclerview to the adapter
                employeeAttendanceRecyclerView.adapter = adapterEmployee
            }
        }else{
            //setting the recyclerview container to this manager layout
            val managerAttendanceRecyclerView = requireView().findViewById<RecyclerView>(R.id.managerAttendanceRecyclerView)
            managerAttendanceRecyclerView.layoutManager = layoutManager

            //get the list of all the attendance records with employee name
            mainViewModel.getListOfAttendanceRecordWithName()
            mainViewModel.listOfAttendanceWithName.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    requireView().findViewById<TextView>(R.id.managerAttendnaceNoItemsTextView).visibility = View.VISIBLE
                    managerAttendanceRecyclerView.visibility = View.GONE
                }else{
                    requireView().findViewById<TextView>(R.id.managerAttendnaceNoItemsTextView).visibility = View.GONE
                    managerAttendanceRecyclerView.visibility = View.VISIBLE
                }
                val reversedList: List<AttendanceWithName> = it.reversed()
                adapterManager = this.context?.let { it1 -> ManagerAttendanceAdapter(it1, reversedList,this) }
                //setting recyclerview to the adapter
                managerAttendanceRecyclerView.adapter = adapterManager
            }

            //initialise a list for all the employee ids
            var employeeIdList : List<Int> = listOf(100)

            mainViewModel.getListOfEmployeeId()
            mainViewModel.listOfEmployeeId.observe(viewLifecycleOwner){
                //parse the all the eid to store in employeeIdList
                employeeIdList = it
            }


            //show pop up dialog for add new attendance record
            btnAddAttendance.setOnClickListener {
                val view = View.inflate(this.context,
                    R.layout.activity_attendance_add,null)
                val builder = AlertDialog.Builder(this.context)
                builder.setView(view)

                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                //dismiss the pop up dialog if user click on the cancle button
                val cancleBtn = view.findViewById<Button>(R.id.cancleAddAttendanceButton)
                cancleBtn.setOnClickListener {
                    dialog.dismiss()
                }

                //validate and process the inputs if user click on the submit button
                val submitBtn = view.findViewById<Button>(R.id.confirmAddAttendanceButton)
                submitBtn.setOnClickListener {
                    val eid = view.findViewById<EditText>(R.id.addEidEditText)
                    val clockIn = view.findViewById<EditText>(R.id.addClockInTimeEditText)
                    val clockOut = view.findViewById<EditText>(R.id.addClockOutTimeEditText)
                    var isLate:Int
                    //check for empty inputs
                    if (eid.text.isNotEmpty() && clockIn.text.isNotEmpty() && clockOut.text.isNotEmpty()){
                        //check for valid datetime format for the clockin and clockout time
                        if(InputValidator.isValidDateTimeFormat(clockIn.text.toString()) && InputValidator.isValidDateTimeFormat(clockOut.text.toString())){
                            //check for valid clockout time (cannot clockout before 18:00:00)
                            if (InputValidator.isValidClockOut(clockOut.text.toString())) {
                                //check for valid eid (to prevent insert exception)
                                if (InputValidator.ifEidIsValid(employeeIdList, eid.text.toString().toInt())){
                                    //determine the value for isLate to insert into database
                                    if (InputValidator.isClockInLate(clockIn.text.toString())) {
                                        isLate = 1
                                    } else {
                                        isLate = 0
                                    }
                                    Toast.makeText(this.context, "the attendance record has been added", Toast.LENGTH_SHORT)
                                        .show()
                                    val attendance = AttendanceRecord(eid.text.toString().toInt(), clockIn.text.toString(), clockOut.text.toString(), isLate)
                                    mainViewModel.insertAttendance(attendance)
                                    dialog.dismiss()
                                    val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentHolder)
                                    val detachTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                    val attachTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                    //refresh the fragment
                                    currentFragment?.let {
                                        detachTransaction.detach(it).commit()
                                        attachTransaction.attach(it).commit()
                                    }
                                }else{
                                    Toast.makeText(this.context, "Your employee id is not valid!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }else{
                                Toast.makeText(this.context, "You are not supposed to clock out before 18:00:00!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }else{
                            Toast.makeText(this.context, "Your datetime format does not matched!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else{
                        Toast.makeText(this.context, "Please key in all the fields!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    //show pop up dialog for edit existing attendance record
    @RequiresApi(Build.VERSION_CODES.O)
    fun editAttendanceRecordDialog(attendance:AttendanceWithName) {
        val view = View.inflate(this.context,
            R.layout.activity_attendance_edit,null)
        val builder = AlertDialog.Builder(this.context)
        builder.setView(view)

        view.findViewById<EditText>(R.id.editClockInTimeEditText).setText(attendance.attendance.clockInTime)
        view.findViewById<EditText>(R.id.editClockOutTimeEditText).setText(attendance.attendance.clockOutTime)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //dismiss the pop up dialog if user click on the cancle button
        val cancleBtn = view.findViewById<Button>(R.id.cancleEditAttendanceButton)
        cancleBtn.setOnClickListener {
            dialog.dismiss()
        }

        //validate and process the inputs if user click on the submit button
        val submitBtn = view.findViewById<Button>(R.id.confirmEditAttendanceButton)
        submitBtn.setOnClickListener {
            val clockIn = view.findViewById<EditText>(R.id.editClockInTimeEditText)
            val clockOut = view.findViewById<EditText>(R.id.editClockOutTimeEditText)
            var isLate:Int
            //check for empty inputs
            if (clockIn.text.isNotEmpty() && clockOut.text.isNotEmpty()){
                //check for valid datetime format for the clockin and clockout time
                    if(InputValidator.isValidDateTimeFormat(clockIn.text.toString()) && InputValidator.isValidDateTimeFormat(clockOut.text.toString())){
                        //check for valid clockout time (cannot clockout before 18:00:00)
                        if (InputValidator.isValidClockOut(clockOut.text.toString())) {
                            //determine the value for isLate to insert into database
                            if (InputValidator.isClockInLate(clockIn.text.toString())) {
                                isLate = 1
                            } else {
                                isLate = 0
                            }
                            val aid = attendance.attendance.aid.toString().toInt()
                            Toast.makeText(this.context, "the attendance record has been edited", Toast.LENGTH_SHORT)
                                .show()
                            mainViewModel.updateAttendance(aid, clockIn.text.toString(), clockOut.text.toString(), isLate)
                            dialog.dismiss()
                            val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentHolder)
                            val detachTransaction = requireActivity().supportFragmentManager.beginTransaction()
                            val attachTransaction = requireActivity().supportFragmentManager.beginTransaction()
                            //refresh the fragment
                            currentFragment?.let {
                                detachTransaction.detach(it).commit()
                                attachTransaction.attach(it).commit()
                            }
                        }else{
                            Toast.makeText(this.context, "You are not supposed to clock out before 6pm!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else{
                        Toast.makeText(this.context, "Your datetime format does not matched!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }else{
                Toast.makeText(this.context, "Please key in all the fields!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    //show pop up dialog for deleting attendance record
    fun deleteAttendanceRecordDialog(aid: Int){
        val view = View.inflate(this.context,
            R.layout.activity_attendance_delete,null)
        val builder = AlertDialog.Builder(this.context)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cancleBtn = view.findViewById<Button>(R.id.cancleDeleteAttendanceButton)
        cancleBtn.setOnClickListener {
            dialog.dismiss()
        }

        val confirmBtn = view.findViewById<Button>(R.id.confirmDeleteAttendanceButton)
        confirmBtn.setOnClickListener {
            mainViewModel.deleteAttendance(aid)
            dialog.dismiss()
            val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentHolder)
            val detachTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val attachTransaction = requireActivity().supportFragmentManager.beginTransaction()
            //refresh the fragment
            currentFragment?.let {
                detachTransaction.detach(it).commit()
                attachTransaction.attach(it).commit()
            }
        }
    }

}