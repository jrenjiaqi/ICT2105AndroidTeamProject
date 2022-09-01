package com.example.ict2105project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.entity.ClaimRecordWithName
import com.example.ict2105project.repository.HRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ManagerClaimsViewModel(private val repo: HRRepository) : ViewModel() {
    //to get the list of employees

    // Create a LiveData with a List<ClaimRecord>
    val listOfClaimRecord: MutableLiveData<List<ClaimRecord>> by lazy {
        MutableLiveData<List<ClaimRecord>>()
    }

    fun getListOfClaims() = viewModelScope.launch(Dispatchers.IO) {
        val claimsList: List<ClaimRecord> = repo.getAllClaimRecords()
        listOfClaimRecord.postValue(claimsList)
    }

    fun getListOfClaimsByStatus(status: String) = viewModelScope.launch(Dispatchers.IO) {
        val claimsList: List<ClaimRecord> = repo.getClaimRecordByStatus(status)
        println(repo.getAllClaimRecords())
        listOfClaimRecord.postValue(claimsList)
    }

    // Create a LiveData with a List<ClaimRecord>
    val listOfClaimRecordWithNameByStatus: MutableLiveData<List<ClaimRecordWithName>> by lazy {
        MutableLiveData<List<ClaimRecordWithName>>()
    }

    fun getListOfClaimRecordWithNameByStatus(status: String) = viewModelScope.launch(Dispatchers.IO) {
        val claimRecordWithNameListByStatus: List<ClaimRecordWithName> = repo.getAllClaimRecordsWithName(status)
        listOfClaimRecordWithNameByStatus.postValue(claimRecordWithNameListByStatus)
    }

    fun updateClaimStatus(cid: Int, status: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateClaimRecordStatus(cid, status)
    }

    fun addOneClaimRecord(eid: Int, date: String, title: String, imageUrl:String, amount: Float, reason: String, status: String)
            = viewModelScope.launch(Dispatchers.IO) {
        // note cid of 0 tells the repo to use autoincrement (as implemented in its entity)
        repo.insertClaimRecord(ClaimRecord(0, eid, date, title, imageUrl, amount, reason, status))
    }
}

class ManagerClaimsViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagerClaimsViewModel::class.java)) {
            return ManagerClaimsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}