package com.example.ict2105project.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.ForeignKey.SET_NULL
import androidx.room.PrimaryKey

/**
 * hrdatabase.Employee
 *
 * eid      |   ename   |   profileImage    |   phoneNumber |   email           |   password    |   role
 * PK,int   |   string  |   bytearray?      |   string      |   unique,string   |   string      |   int
 *
 *
** bug with Room Library, naming doing "name = name" for @ColumnInfo runs it first in Schema
 * hence, ename works
 */

@Entity(tableName = "Employee",
    foreignKeys = [ForeignKey(entity = Role::class,
        parentColumns = ["rid"], childColumns = ["role"],
        onDelete = SET_NULL, onUpdate = NO_ACTION)])
data class Employee (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eid")
    val eid: Int,

    @NonNull
    @ColumnInfo(name = "ename")
    val name: String,

    @NonNull
    @ColumnInfo(name = "profileImage")
    val image: ByteArray?,

    @NonNull
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,

    @NonNull
    @ColumnInfo(name = "email")
    val email: String,

    @NonNull
    @ColumnInfo(name = "password")
    val password: String,

    @NonNull
    @ColumnInfo(name = "role")
    val role: Int
){
    /**
     * To allow specific field insertion using @insert at DAO
     */
    constructor(
        name: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: Int
    ) : this(0, name, null, phoneNumber, email, password, role)
}
