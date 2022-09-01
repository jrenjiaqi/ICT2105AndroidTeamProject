package com.example.ict2105project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.entity.LeaveRecord
import com.example.ict2105project.entity.LeaveRecordWithName

@Dao
interface LeaveRecordDAO {
    /** (TESTED AND WORKING)
     * function to get employee's leave records by eid (for employee view leave)
     *@param employeeId of type Int
     *@return list of LeavesRecord order by status in descending order
     */
    @Query("SELECT * FROM LeaveRecord WHERE LeaveRecord.eid LIKE :eid ORDER BY LeaveRecord.status DESC")
    fun getLeaveRecordsByEid(eid: Int): List<LeaveRecord>

    /** (TESTED AND WORKING)
     * function to get employee's leave records by status (for employee delete leave)
     *@param status of the employee leave of type string
     *@return list of pending LeavesRecord
     */
    @Query("SELECT * FROM LeaveRecord WHERE LeaveRecord.status LIKE:status")
    fun ManagerGetListOfPendingLeaveRecord(status:String): List<LeaveRecord>

    /** (TESTED AND WORKING)
     * function for employee to insert their data
     * @param list of leave record
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLeaveRecord(LeaveRecord: LeaveRecord)

    /** (TESTED AND WORKING)
     * function for employee to delete their leave application if required
     */
    @Query("Delete FROM LeaveRecord WHERE LeaveRecord.eid LIKE:eid AND LeaveRecord.lid LIKE:lid")
    fun deleteLeaveRecord(eid:Int,lid:Int)


    /** (TESTED AND WORKING)
     * function to get employee's leave records by status
     *@param employeeId and status of the leaveRecord
     *@returns list of leaveRecord pending on status
     */
    @Query("SELECT * FROM LeaveRecord WHERE LeaveRecord.eid LIKE :eid AND LeaveRecord.status LIKE :status")
    fun getPendingLeaveRecord(eid:Int,status:String):List<LeaveRecord>

    /** (TESTED AND WORKING)
     * function to get employee's leave records by eid
     * @param leave ID and status
     */
    @Query("UPDATE LeaveRecord SET status = :status WHERE lid = :lid")
    suspend fun updateLeaveRecord(lid: Int, status: String)
    /** (TESTED AND WORKING)
     * function to get employee's name
     * @param status of leave record
     * @return list of leaverecord with name
     */
    @Query("SELECT LeaveRecord.*, Employee.ename FROM LeaveRecord INNER JOIN Employee ON LeaveRecord.eid = Employee.eid WHERE LeaveRecord.status = :status")
    suspend fun getLeaveRecordWithNameByStatus(status: String): List<LeaveRecordWithName>
}