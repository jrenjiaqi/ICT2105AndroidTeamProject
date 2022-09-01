package com.example.ict2105project.dao

import androidx.room.*
import com.example.ict2105project.entity.AttendanceRecord
import com.example.ict2105project.entity.AttendanceWithName

@Dao
interface AttendanceRecordDAO {
    /** (TESTED AND WORKING)
     * function to get all the attendance records
     *
     * @returns list of AttendanceRecord objects
     */
    @Query("SELECT * FROM AttendanceRecord")
    fun getAllAttendanceRecords(): List<AttendanceRecord>

    /** (TESTED AND WORKING)
     * function to get all the attendance records with employee name by joining the AttendanceRecord table and Employee table
     *
     * @return list of AttendanceWithName objects
     */
    @Query("SELECT AttendanceRecord.*, Employee.ename FROM AttendanceRecord INNER JOIN EMPLOYEE ON AttendanceRecord.eid = Employee.eid")
    fun getAllAttendanceRecordWithName(): List<AttendanceWithName>

    /** (TESTED AND WORKING)
     * function to get employee's attendance records by eid
     * @param eid Integer of employee id
     * @return list of AttendanceRecord objects
     */
    @Query("SELECT * FROM AttendanceRecord WHERE AttendanceRecord.eid LIKE :eid")
    fun getAttendanceRecordsByEid(eid: Int): List<AttendanceRecord>

    /** (TESTED AND WORKING)
     * function to update employee's attendance record update by taking in aid, clockinTime and clockoutTime
     * @param aid Integer of attendance record id
     * @param newClcokIn String of new clock in datetime to update
     * @param newClockOut String of new clock out datetime to update
     * @param newIsLate Integer to determine if the attendance record is late (1 = late, 0 = on time)
     * @return the number of rows affected (should only ever be 1)
     */
    @Query("UPDATE AttendanceRecord SET clockIn = :newClockIn, clockOut = :newClockOut, isLate=:newIsLate WHERE aid = :aid")
    fun updateAttendanceRecordByAid(aid: Int, newClockIn: String, newClockOut: String, newIsLate: Int): Int

    /** (TESTED AND WORKING)
     * function to insert new attendance record by taking in ALL attendance record field
     * @param attendance an AttendanceRecord object that contains all its attributes
     * @return the number of the newly generated ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttendanceRecord(attendance:AttendanceRecord) : Long

    /** (TESTED AND WORKING)
     * function to delete attendance record by aid
     * @param aid Integer of attendance record id
     * @return the number of rows affected (should only ever be 1)
     */
    @Query("DELETE FROM AttendanceRecord WHERE aid = :aid")
    fun deleteAttendanceRecordByAid(aid: Int)

    /** (TESTED AND WORKING)
     * function to add a clock out time for by eid
     * @param aid Integer of attendance record id
     * @param clockout String of clock out datetime to update
     * @return the number of rows affected (should only ever be 1)
     */
    @Query("UPDATE AttendanceRecord SET clockOut = :clockOut WHERE aid = :aid")
    fun clockOutAttendanceRecordByEid(aid: Int, clockOut: String) : Int
}