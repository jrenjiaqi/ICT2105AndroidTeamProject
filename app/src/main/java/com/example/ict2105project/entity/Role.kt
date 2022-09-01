package com.example.ict2105project.entity

import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION

/**
 * employee_db.role
 *
 * rid  |   role    |
 * PK   |   string  |
 *
 */
@Entity(tableName = "Role")
data class Role (
    @PrimaryKey
    @ColumnInfo(name = "rid")
    val rid: Int,

    @NonNull
    @ColumnInfo(name = "name")
    val name: String
)