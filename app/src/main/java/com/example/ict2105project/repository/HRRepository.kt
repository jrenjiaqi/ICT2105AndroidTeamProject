package com.example.ict2105project.repository

import com.example.ict2105project.dao.*
import com.example.ict2105project.entity.*

/**local SQLite database,
 * check DAO package for their respective DAO and their corresponding comments
 */

class HRRepository(private val employeeDAO: EmployeeDAO,
                   private val roleDAO: RoleDAO,
                   private val attendanceRecordDAO: AttendanceRecordDAO,
                   private val claimRecordDAO: ClaimRecordDAO,
                   private val leaveRecordDAO: LeaveRecordDAO) {
    /** AttendanceRecordDAO
     * function to get all the attendance records
     *
     * @returns list of AttendanceRecord objects
     */
    suspend fun getAllAttendanceRecords(): List<AttendanceRecord>{
        return attendanceRecordDAO.getAllAttendanceRecords()
    }

    /** AttendanceRecordDAO
     * function to get all the attendance records with employee name by joining the AttendanceRecord table and Employee table
     *
     * @return list of AttendanceWithName objects
     */
    suspend fun getAllAttendanceRecordWithName(): List<AttendanceWithName>{
        return attendanceRecordDAO.getAllAttendanceRecordWithName()
    }

    /** AttendanceRecordDAO
     * @param eid Integer of employee id
     * @return list of AttendanceRecord objects
     */
    suspend fun getAllAttendanceRecordsByEid(eid: Int): List<AttendanceRecord>{
        return attendanceRecordDAO.getAttendanceRecordsByEid(eid)
    }

    /** AttendanceRecordDAO
     * function to update employee's attendance record update by taking in aid, clockinTime and clockoutTime
     * @param aid Integer of attendance record id
     * @param newClcokIn String of new clock in datetime to update
     * @param newClockOut String of new clock out datetime to update
     * @param newIsLate Integer to determine if the attendance record is late (1 = late, 0 = on time)
     * @return the number of rows affected (should only ever be 1)
     */
    suspend fun updateAttendanceRecord(aid: Int, newClockIn: String, newClockOut: String, newIsLate: Int): Int{
        return attendanceRecordDAO.updateAttendanceRecordByAid(aid, newClockIn, newClockOut, newIsLate)
    }

    /** AttendanceRecordDAO
     * function to insert new attendance record by taking in ALL attendance record field
     * @param attendance an AttendanceRecord object that contains all its attributes
     * @return the number of the newly generated ID
     */
    suspend fun insertAttendanceRecord(attendance: AttendanceRecord) : Long {
        return attendanceRecordDAO.insertAttendanceRecord(attendance)
    }
    
    /** AttendanceRecordDAO
     * @param aid Integer of attendance record id
     * @param clockout String of clock out datetime to update
     * @return the number of rows affected (should only ever be 1)
     */ 
    suspend fun clockOutAttendanceRecord(aid: Int, clockOutTime: String) : Int {
        return attendanceRecordDAO.clockOutAttendanceRecordByEid(aid, clockOutTime)
    }

    /** AttendanceRecordDAO
     * function to delete attendance record by aid
     * @param aid Integer of attendance record id
     * @return the number of rows affected (should only ever be 1)
     */
    suspend fun deleteAttendanceRecord(aid: Int) {
        return attendanceRecordDAO.deleteAttendanceRecordByAid(aid)
    }

    /** ClaimRecordDAO
     * function to get employee's claim records by eid
     * @param eid employee ID
     * @returns list of ClaimRecord objects
     */
    suspend fun getClaimRecordsByEid(eid: Int): List<ClaimRecord>{
        return claimRecordDAO.getClaimRecordsByEid(eid)
    }

    /** ClaimRecordDAO
     * function to get employee's claim records by eid and status
     * @returns list of ClaimRecord objects
     */
    suspend fun getClaimRecordsByEidAndStatus(eid: Int, status:String): List<ClaimRecord> {
        return claimRecordDAO.getClaimRecordsByEidAndStatus(eid, status)
    }

    /** ClaimRecordDAO
     * function to get all employee's claim records
     * @return list of ClaimRecord objects
     */
    suspend fun getAllClaimRecords(): List<ClaimRecord>{
        return claimRecordDAO.getClaimRecords()
    }

    /** ClaimRecordDAO
     * function to get employee's claim records by status, with employee name
     * @param status the status of the application
     * @returns list of ClaimRecordWithName objects
     */
    suspend fun getAllClaimRecordsWithName(status: String): List<ClaimRecordWithName> {
        return claimRecordDAO.getAllClaimRecordsWithNameByStatus(status)
    }
    /** ClaimRecordDAO
     * function to insert an employee claim record
     * @param claimRecord a claim record entity
     */
    suspend fun insertClaimRecord(claimRecord: ClaimRecord) {
        claimRecordDAO.insertClaimRecord(claimRecord)
    }

    /** ClaimRecordDAO
     * update a claim record by its claim id and status
     * @param cid claim id
     * @param status application status
     */
    suspend fun updateClaimRecordStatus(cid: Int, status: String) {
        claimRecordDAO.updateClaimRecord(cid, status)
    }

    /** ClaimRecordDAO
     * function to update the status of a claim record
     * @param status application status
     * @return list of claim record objects
     */
    suspend fun getClaimRecordByStatus(status: String): List<ClaimRecord> {
        return claimRecordDAO.getClaimRecordsByStatus(status)
    }

    /** EmployeeDAO
     * get an employee object by eid
     *
     * @param eid of current logged in employee
     * @return Employee object containing all details get by eid
     */
    suspend fun getEmployee(eid: Int): Employee{
        return employeeDAO.getEmployeeByEid(eid)
    }

    /** EmployeeDAO
     * get employee object by email
     *
     * @param email of current logged in employee
     * @returns an Employee object
     */
    suspend fun getEmployeeByEmail(email: String): Employee{
        return employeeDAO.getEmployeeByEmail(email)
    }

    /** EmployeeDAO
     * get employee image by eid
     *
     * @param eid of current logged in employee
     * @return ByteArray? containing employee profile image get by eid
     */
    suspend fun getEmployeeImage(eid: Int): ByteArray?{
        return employeeDAO.getEmployeeImageByEid(eid)
    }

    /** EmployeeDAO
     * get employee password by eid
     *
     * @param eid of current logged in employee
     * @return String containing employee hashed password get by eid
     */
    suspend fun getEmployeePassword(eid: Int): String{
        return employeeDAO.getEmployeePasswordByEid(eid)
    }

    /** EmployeeDAO
     * to insert new employee info
     *
     * @param employee object containing new employee details
     * @return Long containing newly created id
     */
    suspend fun insertNewEmployeeInfo(employee: Employee): Long{
        return employeeDAO.insertNewEmployee(employee)
    }

    /** EmployeeDAO
     * to update/edit employee info by eid
     *
     * @param eid of current logged in employee
     * @param newName changed by current logged in employee
     * @param newPhoneNumber changed in by of current logged in employee
     * @param newEmail changed by of current logged in employee
     * @return Boolean true or false whether updating employee information is successful
     */
    suspend fun updateEmployeeInfo(eid: Int, newName: String,
                                   newPhoneNumber: String, newEmail: String) : Boolean{
        return employeeDAO.updateEmployeeByEid(eid, newName, newPhoneNumber, newEmail) == 1
    }

    /** EmployeeDAO
     * to update employee new profile image by eid
     *
     * @param eid of current logged in employee
     * @param newImage an employee profile image captured by current logged in employee
     * @return Boolean true or false whether updating employee profile image is successful
     */
    suspend fun updateEmployeeImage(eid: Int, newImage: ByteArray) : Boolean{
        return employeeDAO.updateEmployeeImageByEid(eid, newImage) == 1
    }

    /** EmployeeDAO
     * to update employee new password by eid
     *
     * @param eid of current logged in employee
     * @param newPassword a new password created by current logged in employee
     * @return Boolean true or false whether updating employee new password is successful
     */
    suspend fun updateEmployeePassword(eid: Int, newPassword: String) : Boolean{
        return employeeDAO.updateEmployeePassByEid(eid, newPassword) == 1
    }

    /** EmployeeDAO
     * to get all the employee id
     */
    suspend fun getAllEmployeeId() : List<Int>{
        return employeeDAO.getAllEmployeeId()
    }

    /** EmployeeDAO
     * to login for the user
     */
    suspend fun loginUser(email: String, password: String): Boolean{
        val resFromDB: Int= employeeDAO.logInEmployee(email, password)
        if(resFromDB == 1){ //1 match in DB
            return true
        }
        return false //no matches in database
    }

    /** RoleRecordDAO
     * get an employee role details by rid
     *
     * @param rid a role id of current logged in employee
     * @return String containing a role name get by rid
     */
    suspend fun getEmployeeRole(rid: Int): String{
        return roleDAO.getEmployeeRole(rid)
    }

    /** RoleRecordDAO
     * get rid by role description
     *
     * @return List<Role> containing list of role id and role name
     */
    suspend fun getAllRole(): List<Role>{
        return roleDAO.getAllRole()
    }

    /** LeaveRecordDAO
     * function to get all leaves record by eid
     * @param employeeID
     */
    suspend fun getAllLeaveRecords(eid: Int): List<LeaveRecord>{
        return leaveRecordDAO.getLeaveRecordsByEid(eid)
    }
    /** LeaveRecordDAO
     * function to insert a leave record into db
     * @param leaveRecord
     */
    suspend fun InsertLeaveRecord(LeaveRecord: LeaveRecord) {
        leaveRecordDAO.insertLeaveRecord(LeaveRecord)
    }
    /** LeaveRecordDAO
     * function to update status of leave record by the leave id
     * @param leaveId and status of that specific leave
     */
    suspend fun updateLeaveRecord(lid: Int, status: String) {
        leaveRecordDAO.updateLeaveRecord(lid, status)
    }
    /** LeaveRecordDAO
     * get list of pending leave record (for employee view purpose)
     * @param employeeID and status of the leaveRecord
     */
    suspend fun getPendingLeaveRecord(eid:Int,status:String):List<LeaveRecord>{
        return leaveRecordDAO.getPendingLeaveRecord(eid,status)
    }
    /** LeaveRecordDAO
     * get list of pending leave record (for manager manage leave purpose)
     * @param leaveStatus
     */
    suspend fun ManagerGetListOfPendingLeaveRecord(status:String):List<LeaveRecord>{
        return leaveRecordDAO.ManagerGetListOfPendingLeaveRecord(status)
    }
    /** LeaveRecordDAO
     * function to delete a leave record (if employee wish to redraw their application)
     * @param employeeID and leaveId
     */
    suspend fun deleteLeaveRecord(eid: Int,lid:Int){
        leaveRecordDAO.deleteLeaveRecord(eid,lid)
    }
    /** LeaveRecordDAO
     * function to get the name of the employee by status
     * @param leaveStatus
     */
    suspend fun getLeaveRecordWithNameByStatus(status: String): List<LeaveRecordWithName> {
        return leaveRecordDAO.getLeaveRecordWithNameByStatus(status)
    }

}