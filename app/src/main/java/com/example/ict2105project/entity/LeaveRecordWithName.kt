package com.example.ict2105project.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

class LeaveRecordWithName {
    @Embedded
    lateinit var leaveRecord: LeaveRecord

    @ColumnInfo(name = "ename")
    lateinit var ename: String
}