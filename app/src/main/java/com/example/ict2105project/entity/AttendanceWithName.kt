package com.example.ict2105project.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

class AttendanceWithName {
    @Embedded
    lateinit var attendance: AttendanceRecord

    @ColumnInfo(name = "ename")
    lateinit var ename:String
}