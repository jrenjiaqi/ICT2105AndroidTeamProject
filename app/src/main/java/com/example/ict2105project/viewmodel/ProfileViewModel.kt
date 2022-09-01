package com.example.ict2105project.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.Employee
import com.example.ict2105project.entity.Role
import com.example.ict2105project.repository.HRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: HRRepository) : ViewModel() {

    //declare mutable live data variable
    var listOfRoles = MutableLiveData<List<Role>>()
    var employeeImage = MutableLiveData<ByteArray?>()
    var employeePassword = MutableLiveData<String>()
    var failedInsertMessage = MutableLiveData<String?>()
    var isInfoInserted = MutableLiveData<Boolean>()
    var isInfoUpdated = MutableLiveData<Boolean>()
    var isImageUpdated = MutableLiveData<Boolean>()
    var isPasswordUpdated = MutableLiveData<Boolean>()

    //to get rid based on role description
    fun getAllRole() = viewModelScope.launch(Dispatchers.IO) {
        val list: List<Role> = repo.getAllRole()
        listOfRoles.postValue(list)
    }

    /**
     * to get employee password based on eid
     * @param eid of current logged in user
     */
    fun getEmployeeImage(eid: Int) = viewModelScope.launch(Dispatchers.IO) {
        val image: ByteArray? = repo.getEmployeeImage(eid)
        employeeImage.postValue(image)
    }

    /**
     * to get employee password based on eid
     * @param eid of current logged in user
     */
    fun getEmployeePassword(eid: Int) = viewModelScope.launch(Dispatchers.IO) {
        val password: String = repo.getEmployeePassword(eid)
        employeePassword.postValue(password)
    }

    /**
     * to insert new employee
     * @param employee containing new employee details
     */
    fun insertNewEmployeeInfo(employee: Employee) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repo.insertNewEmployeeInfo(employee)
            isInfoInserted.postValue(true)
            failedInsertMessage.postValue(null)
        } catch (e: SQLiteConstraintException) {
            isInfoInserted.postValue(false)
            failedInsertMessage.postValue("Email Exist!")
        }
    }

    /**
     * to update single employee based on eid
     * @param eid of current logged in user
     * @param newName changed by current logged in employee
     * @param newPhoneNumber changed in by of current logged in employee
     * @param newEmail changed by of current logged in employee
     */
    fun updateEmployeeById(eid: Int, newName: String, newPhoneNumber:String, newEmail: String) = viewModelScope.launch(Dispatchers.IO) {
        isInfoUpdated.postValue(repo.updateEmployeeInfo(eid, newName, newPhoneNumber, newEmail))
    }

    /**
     * to update employee profile image based on eid
     * @param eid of current logged in employee
     * @param newImage an employee profile image captured by current logged in employee
     */
    fun updateEmployeeImage(eid: Int, newImage: ByteArray) = viewModelScope.launch(Dispatchers.IO) {
        isImageUpdated.postValue(repo.updateEmployeeImage(eid, newImage))
    }

    /**
     * to update employee password based on eid
     * @param eid of current logged in employee
     * @param newPassword a new password created by current logged in employee
     */
    fun updateEmployeePassword(eid: Int, newPassword: String) = viewModelScope.launch(Dispatchers.IO) {
        isPasswordUpdated.postValue(repo.updateEmployeePassword(eid, newPassword))
    }

}

/**
 * @param repo a repository for data retrieving
 * @return ViewModelProvider.Factory a factory for view model
 */
class ProfileViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}