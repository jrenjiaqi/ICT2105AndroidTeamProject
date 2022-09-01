package com.example.ict2105project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.AttendanceRecord
import com.example.ict2105project.entity.AttendanceWithName
import com.example.ict2105project.entity.Employee
import com.example.ict2105project.repository.HRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repo: HRRepository) : ViewModel() {

    //Initialise mutable live data
    var employeeData = MutableLiveData<Employee>()
    var employeeRole = MutableLiveData<String>()
    var listOfEmployeeId = MutableLiveData<List<Int>>()
    var listOfAttendanceRecords = MutableLiveData<List<AttendanceRecord>>()
    var listOfAttendanceRecordByEid = MutableLiveData<List<AttendanceRecord>>()
    var listOfAttendanceWithName = MutableLiveData<List<AttendanceWithName>>()

    /**
     * function to get a list all employee ids
     * @return a list of Integer to contain all the employee ids
     */
    fun getListOfEmployeeId() = viewModelScope.launch(Dispatchers.IO){
        val list: List<Int> = repo.getAllEmployeeId()
        listOfEmployeeId.postValue(list)
    }

    /**
     * function to get an employee details based on employee id
     * @param eid Integer of employee id
     * @return an employee object
     */
    fun getEmployee(eid: Int) = viewModelScope.launch(Dispatchers.IO) {
        val emp: Employee = repo.getEmployee(eid)
        employeeData.postValue(emp)
    }

    /**
     * function to get an employee role based on role id
     * @param eid Integer of role id
     * @return string of employee role name
     */
    fun getEmployeeRole(rid: Int) = viewModelScope.launch(Dispatchers.IO) {
        val role: String = repo.getEmployeeRole(rid)
        employeeRole.postValue(role)
    }

    /**
     * function to get a list of attendance records
     * @return a list of all attendance records
     */
    fun getListOfAttendanceRecords() = viewModelScope.launch(Dispatchers.IO) {
        val list: List<AttendanceRecord> = repo.getAllAttendanceRecords()
        listOfAttendanceRecords.postValue(list)
    }

    /**
     * function to get a list of attendance records based on employee id
     * @param eid employee id
     * @return a list of attendance records
     */
    fun getListOfAttendanceRecordsByEid(eid: Int) = viewModelScope.launch(Dispatchers.IO){
        val list: List<AttendanceRecord> = repo.getAllAttendanceRecordsByEid(eid)
        listOfAttendanceRecordByEid.postValue(list)
    }

    /**
     * function to get a list of attendance records with employee name
     * @return a list of attendance with employee name
     */
    fun getListOfAttendanceRecordWithName() = viewModelScope.launch(Dispatchers.IO) {
        val list: List<AttendanceWithName> = repo.getAllAttendanceRecordWithName()
        listOfAttendanceWithName.postValue(list)
    }

    /**
     * function to update attendance record based on attendance id
     * @return the number of rows affected (should only ever be 1)
     */
    fun updateAttendance(aid: Int, newClockIn: String, newClockOut: String, newIsLate: Int)= viewModelScope.launch(Dispatchers.IO){
        repo.updateAttendanceRecord(aid, newClockIn, newClockOut, newIsLate)
    }

    /**
     * function to insert a new attendance record
     * @return the number of newly generated ID
     */
    fun insertAttendance(attendance: AttendanceRecord) = viewModelScope.launch(Dispatchers.IO){
        repo.insertAttendanceRecord(attendance)
    }

    /**
     * function to insert a new attendance record
     * @return the number of rows affected (should only ever be 1)
     */
    fun deleteAttendance(aid: Int) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteAttendanceRecord(aid)
    }
}

class MainViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}