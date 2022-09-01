package com.example.ict2105project.entity

import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.*

/**
 * hrdatabase.LeaveRecord
 *
 * lid      |   eid     |   startDate   |   endDate |   reason        |   leaveType   |   status
 * PK, int  |   FK, int |   String      |   String  |  String="nil"   |   String      |   String
 *
 *
 */
@Entity(tableName = "LeaveRecord",
        foreignKeys = [ForeignKey(entity = Employee::class,
            parentColumns = ["eid"], childColumns = ["eid"],
            onDelete = NO_ACTION, onUpdate = NO_ACTION)])
data class LeaveRecord (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lid")
    val lid: Int,

    @NonNull
    @ColumnInfo(name = "eid")
    val eid: Int,

    @NonNull
    @ColumnInfo(name = "startDate")
    val startDate: String,

    @NonNull
    @ColumnInfo(name = "endDate")
    val endDate: String,

    @NonNull
    @ColumnInfo(name = "reason")
    val reason: String,

    @NonNull
    @ColumnInfo(name = "leaveType")
    val leaveType: String,

    @NonNull
    @ColumnInfo(name = "status")
    val status: String
){
    constructor(
        eid: Int,
        startDate: String,
        endDate: String,
        reason: String?,
        leaveType: String,
        status: String
    ) : this (0, eid, startDate, endDate, reason?:"nil", leaveType, status)
}

