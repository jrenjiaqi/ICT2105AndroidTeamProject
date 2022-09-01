package com.example.ict2105project.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.AttendanceRecord
import com.example.ict2105project.repository.HRRepository
import com.example.ict2105project.utilities.InputValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class HomeViewModel (private val repo: HRRepository) : ViewModel() {
    //to clockin
    var isClockedIn = MutableLiveData<Boolean>(false) //default is false
    var isLate = MutableLiveData<Boolean>(false) //default is false
    var isAtCompany = MutableLiveData<Boolean>()
    @RequiresApi(Build.VERSION_CODES.O)
    fun clockInEmployee(eid: Int, clockInTime: String, clockOutTime: String, currentLocation: String) = viewModelScope.launch(Dispatchers.IO){
        if (currentLocation.contains("Singapore")){
            isAtCompany.postValue(true)
            //check if the user is late
            if (!InputValidator.isClockInLate(clockInTime)) { //if punctual
                val attendanceRecord = AttendanceRecord(eid, clockInTime, clockOutTime, 0)
                repo.insertAttendanceRecord(attendanceRecord).also {
                    if (it != 0L) {
                        isClockedIn.postValue(true)
                        isLate.postValue(false)
                    }
                }
            } else{ //if late
                val attendanceRecord = AttendanceRecord(eid, clockInTime, clockOutTime, 1)
                repo.insertAttendanceRecord(attendanceRecord).also {
                    if(it != 0L){
                        isClockedIn.postValue(true)
                        isLate.postValue(true)
                    }
                }
            }
            isIn = true //for the work around
        } else{
            isAtCompany.postValue(false)
            isErr = true //for the workaround
        }
    }
    var isClockOut = MutableLiveData<Boolean>()
    @RequiresApi(Build.VERSION_CODES.O)
    fun clockOutEmployee(eid: Int, clockOutTime: String) = viewModelScope.launch(Dispatchers.IO){
        //getting the last attendance record generated
        val attendanceList : List<AttendanceRecord> = repo.getAllAttendanceRecordsByEid(eid)
        val todayAid = attendanceList.last().aid
        if(repo.clockOutAttendanceRecord(todayAid, clockOutTime) == 1){
            isClockedIn.postValue(false)
            isIn = false
            isClockOut.postValue(true)
        }
    }
    companion object{
        //https://github.com/leandroBorgesFerreira/LoadingButtonAndroid/issues/150
        //to persist the checkin button text state because library issue with saving states
        //below is a simple work around
        var isIn :Boolean ?= null //to store the checked in
        var isErr :Boolean ?= null  //to store the error checking in
    }
}

class HomeViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
                return HomeViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown class")
        }
    }