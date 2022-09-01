package com.example.ict2105project.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ict2105project.entity.Employee
import com.example.ict2105project.repository.HRRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(private val repo: HRRepository) : ViewModel() {
    //to log in
    var isLoggedIn = MutableLiveData<Boolean>()
    val user = MutableLiveData<Employee>()
    lateinit var emp: Employee
    fun logInEmployee(email: String, password: String) = viewModelScope.launch(Dispatchers.IO){
        if (repo.loginUser(email, password)){
            //posting the employee object so as to be placed into sharedpreferences
            emp = repo.getEmployeeByEmail(email)
            user.postValue(emp)

            isLoggedIn.postValue(true)
        }
        else{
            isLoggedIn.postValue(false)
        }
    }
    //to log out
    fun logOutEmployee(){
        isLoggedIn = MutableLiveData<Boolean>()
    }
}

class LoginViewModelFactory(private val repo: HRRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}