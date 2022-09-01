package com.example.ict2105project.entity

import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.*

/**
 * hrdatabase.AttendanceRecord
 *
 * aid      |   eid     |   clockIn    |   clockOut    |   isLate
 * PK,int   |   FK,int  |   string     |   string      |   int
 *
 *
 ** bug with Room Library, naming doing "name = name" for @ColumnInfo runs it first in Schema
 * hence, ename works
 *
 */

@Entity(tableName = "AttendanceRecord",
        foreignKeys = [ForeignKey(entity = Employee::class,
            parentColumns = ["eid"], childColumns = ["eid"],
            onDelete = NO_ACTION, onUpdate = NO_ACTION)])
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "aid")
    val aid: Int,

    @NonNull
    @ColumnInfo(name = "eid")
    val eid: Int,

    @NonNull
    @ColumnInfo(name = "clockIn")
    val clockInTime: String,

    @NonNull
    @ColumnInfo(name = "clockOut")
    val clockOutTime: String,

    @NonNull
    @ColumnInfo(name = "isLate")
    val isLate: Int
){
    constructor(eid: Int, clockIn: String, clockOut: String, isLate: Int) : this (0, eid, clockIn, clockOut, isLate)
}