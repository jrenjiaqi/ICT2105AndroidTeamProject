package com.example.ict2105project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ict2105project.entity.Employee

@Dao
interface EmployeeDAO {
    /** (TESTED AND WORKING)
     * function to get a single employee by eid
     *
     * @param eid of current logged in employee
     * @return Employee object containing all details get by eid
     */
    @Query("SELECT * FROM employee WHERE employee.eid LIKE :eid")
    fun getEmployeeByEid(eid: Int): Employee

    /** (TESTED AND WORKING)
     * function to get a employee's profile image by eid
     *
     * @param eid of current logged in employee
     * @return ByteArray? containing employee profile image get by eid
     */
    @Query("SELECT profileImage FROM employee WHERE employee.eid LIKE :eid")
    fun getEmployeeImageByEid(eid: Int): ByteArray?

    /** (TESTED AND WORKING)
     * function to get a single employee by email
     *
     * @param eid of current logged in employee
     * @return String containing employee hashed password get by eid
     */
    @Query("SELECT password FROM employee WHERE employee.eid LIKE :eid")
    fun getEmployeePasswordByEid(eid: Int): String

    /** (TESTED AND WORKING)
     * function to insert new employee's personal information
     *
     * @param employee object containing new employee details
     * @return Long containing newly created id
     */
    @Insert
    fun insertNewEmployee(employee: Employee): Long

    /** (TESTED AND WORKING)
     * function to update employee's personal information by eid
     *
     * @param eid of current logged in employee
     * @param newName changed by current logged in employee
     * @param newPhoneNumber changed in by of current logged in employee
     * @param newEmail changed by of current logged in employee
     * @return Int 1 or 0whether updating employee information is successful
     */
    @Query("UPDATE employee SET ename = :newName, phoneNumber = :newPhoneNumber, email = :newEmail WHERE eid = :eid")
    fun updateEmployeeByEid(eid: Int, newName: String, newPhoneNumber:String, newEmail: String): Int

    /** (TESTED AND WORKING)
     * function to get a single employee by email
     *
     * @param email of current logged in employee
     * @returns an Employee object
     */
    @Query("SELECT * FROM employee WHERE employee.email LIKE :email")
    fun getEmployeeByEmail(email: String): Employee

    /** (TESTED AND WORKING)
     * function to update employee's profile image by eid
     *
     * @param eid of current logged in employee
     * @param newImage an employee profile image captured by current logged in employee
     * @return Int 1 or 0 whether updating employee profile image is successful
     */
    @Query("UPDATE employee SET profileImage = :newImage WHERE eid = :eid")
    fun updateEmployeeImageByEid(eid: Int, newImage: ByteArray): Int

    /** (TESTED AND WORKING)
     * function to update employee's password by eid
     *
     * @param eid of current logged in employee
     * @param newPassword a new password created by current logged in employee
     * @return Int 1 or 0 whether updating employee new password is successful
     */
    @Query("UPDATE employee SET password = :newPassword WHERE eid = :eid")
    fun updateEmployeePassByEid(eid: Int, newPassword: String): Int

    /** (TESTED AND WORKING)
     * function to get all employees and their details
     *
     * @returns List<Int> the number of rows matched (should only ever be 1)
     */
    @Query("SELECT eid FROM employee")
    fun getAllEmployeeId(): List<Int>

    /** (TESTED AND WORKING)
     * function to login employee into the system by checking corresponding fields that was entered
     *
     * @param email of an employee who attempted to log in
     * @param password a hashed password of an employee who attempted to log in
     * @returns Int the number of rows matched (should only ever be 1)
     */
    @Query("SELECT COUNT(employee.eid) FROM employee WHERE employee.email LIKE :email AND employee.password LIKE :password")
    fun logInEmployee(email: String, password: String): Int
}