package com.example.ict2105project.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.enum.ClaimLeaveStatusEnum
import com.example.ict2105project.repository.HRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ClaimsViewModel(private val repo: HRRepository) : ViewModel() {
    //to get the list of employees

    // Create a LiveData with a List<ClaimRecord>
    val listOfApprovedClaimRecord: MutableLiveData<List<ClaimRecord>> by lazy {
        MutableLiveData<List<ClaimRecord>>()
    }
    val listOfRejectedClaimRecord: MutableLiveData<List<ClaimRecord>> by lazy {
        MutableLiveData<List<ClaimRecord>>()
    }
    val listOfPendingClaimRecord: MutableLiveData<List<ClaimRecord>> by lazy {
        MutableLiveData<List<ClaimRecord>>()
    }
    fun getListOfClaimsByEidAndStatus(eid: Int, status: String) = viewModelScope.launch(Dispatchers.IO) {
        val claimsList: List<ClaimRecord> = repo.getClaimRecordsByEidAndStatus(eid, status)
//        println("getting: " + claimsList)
        when {
            status == ClaimLeaveStatusEnum.APPROVED.status -> {
                listOfApprovedClaimRecord.postValue(claimsList)
                Log.d(">>", "HIT approve!")
            }
            status == ClaimLeaveStatusEnum.REJECTED.status -> {
                listOfRejectedClaimRecord.postValue(claimsList)
                Log.d(">>", "HIT reject!")
            }
            status == ClaimLeaveStatusEnum.PENDING.status -> listOfPendingClaimRecord.postValue(claimsList)
        }
    }

    fun addOneClaimRecord(eid: Int, date: String, type: String, imageUrl:String, amount: Float, reason: String, status: String)
        = viewModelScope.launch(Dispatchers.IO) {
        // note cid of 0 tells the repo to use autoincrement (as implemented in its entity)
        repo.insertClaimRecord(ClaimRecord(0, eid, date, type, imageUrl, amount, reason, status))
    }
}

class ClaimsViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimsViewModel::class.java)) {
            return ClaimsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}