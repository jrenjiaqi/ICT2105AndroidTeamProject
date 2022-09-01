package com.example.ict2105project.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

/**
 * hrdatabase.ClaimRecordWithName
 *
 * an embedded ClaimRecord object, with a String of the employee name
 *
 * claimRecord    |   ename     |
 * ClaimRecord    |   String    |
 *
 */
class ClaimRecordWithName {
    @Embedded
    lateinit var claimRecord: ClaimRecord

    @ColumnInfo(name = "ename")
    lateinit var ename: String
}