package com.example.ict2105project.dao

import androidx.room.*
import com.example.ict2105project.entity.ClaimRecord
import com.example.ict2105project.entity.ClaimRecordWithName

/**
 * Claims Data Access Object
 */
@Dao
interface ClaimRecordDAO {
    /** function to get employee's claim records by eid
     * @returns list of ClaimRecord objects
     */
    @Query("SELECT * FROM ClaimRecord WHERE ClaimRecord.eid LIKE :eid")
    fun getClaimRecordsByEid(eid: Int): List<ClaimRecord>

    /** function to get employee's claim records by eid and status
     * @returns list of ClaimRecord objects
     */
    @Query("SELECT * FROM ClaimRecord WHERE ClaimRecord.eid LIKE :eid AND ClaimRecord.status LIKE :status")
    fun getClaimRecordsByEidAndStatus(eid: Int, status: String): List<ClaimRecord>

    /** function to get employee's claim records by status, with employee name
     * @returns list of ClaimRecordWithName objects
     */
    @Query("SELECT ClaimRecord.*, Employee.ename FROM ClaimRecord INNER JOIN Employee ON ClaimRecord.eid = Employee.eid WHERE ClaimRecord.status = :status")
    fun getAllClaimRecordsWithNameByStatus(status: String): List<ClaimRecordWithName>

    /** function to get all employee's claim records
     * @return list of ClaimRecord objects
     */
    @Query("SELECT * FROM ClaimRecord")
    fun getClaimRecords(): List<ClaimRecord>

    /** function to get employee's claim records by status
     * @param status of claim records
     * @return list of ClaimRecord objects
     */
    @Query("SELECT * FROM ClaimRecord WHERE status = :status")
    fun getClaimRecordsByStatus(status: String): List<ClaimRecord>

    /**
     * function to insert an employee claim record
     * @param claimRecord a claim record entity
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClaimRecord(claimRecord: ClaimRecord)

    /**
     * function to update the status of a claim record
     * @param cid claim id
     * @param status employee claim record status ("Pending" / "Approved" / "Rejected")
     */
    @Query("UPDATE ClaimRecord SET status = :status WHERE cid = :cid")
    suspend fun updateClaimRecord(cid: Int, status: String)
}