package com.example.ict2105project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.AttendanceRecord
import com.example.ict2105project.entity.LeaveRecord
import com.example.ict2105project.entity.LeaveRecordWithName
import com.example.ict2105project.repository.HRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LeaveViewModel(private val repo: HRRepository) : ViewModel(){

    //live data to observe changes of the leave record
    var listOfLeaveRecords = MutableLiveData<List<LeaveRecord>>()
    /*
    * function to get list of leave record
    * @param employeeID
    * */
    fun getListOfLeaveRecords(eid: Int) = viewModelScope.launch(Dispatchers.IO) {
        val list: List<LeaveRecord> = repo.getAllLeaveRecords(eid)
        listOfLeaveRecords.postValue(list)
    }
    /*
    * function to get list of pending leave record
    * @param employeeID and status of the leaveRecord
    * */
    var listOfPendingLeaveRecords = MutableLiveData<List<LeaveRecord>>()
    fun getListOfPendingLeaveRecord(eid:Int,status:String) = viewModelScope.launch(Dispatchers.IO) {
        val list: List<LeaveRecord> = repo.getPendingLeaveRecord(eid,status)
        listOfPendingLeaveRecords.postValue(list)
    }

    /*
    * function to get list of name for the employeee
    * @param leave status
    * */
    var managerGetListOfLeaveRecordWithNameByStatus = MutableLiveData<List<LeaveRecordWithName>>()
    fun managerGetListOfLeaveRecordWithNameByStatus(status: String) = viewModelScope.launch(Dispatchers.IO) {
        val list: List<LeaveRecordWithName> = repo.getLeaveRecordWithNameByStatus(status)
        managerGetListOfLeaveRecordWithNameByStatus.postValue(list)
    }
    /*
    * function to update status of a leave record by leave id
    * @param leave ID and status of the leave record
    * */
    fun updateLeaveStatus(lid: Int, status: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateLeaveRecord(lid, status)
    }
    /*
    * function to insert a leave record
    * @param leave record
    * */
    fun insertRecord(LeaveRecord: LeaveRecord) = viewModelScope.launch(Dispatchers.IO) {
        repo.InsertLeaveRecord(LeaveRecord)
    }
    /*
    * function to delete a leave record according to the employee id and the leave id
    * @param employee ID and leave ID
    * */
    fun deleteRecord(eid:Int,lid:Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteLeaveRecord(eid,lid)
    }



}

class LeaveViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaveViewModel::class.java)) {
            return LeaveViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}