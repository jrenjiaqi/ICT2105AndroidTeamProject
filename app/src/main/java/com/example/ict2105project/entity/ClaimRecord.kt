package com.example.ict2105project.entity

import android.media.Image
import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.*

/**
 * hrdatabase.ClaimRecord
 *
 * cid      |   eid     |   date    |   type      |  image_url  |   amount    |   reason    |  status
 * PK,int   |   FK, int |   String  |   String    |   String    |    Float    |   String    |  String
 *
 */

@Entity(tableName = "ClaimRecord",
        foreignKeys = [ForeignKey(entity = Employee::class,
        parentColumns = ["eid"], childColumns = ["eid"],
        onDelete = NO_ACTION, onUpdate = NO_ACTION)])
data class ClaimRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cid")
    val cid: Int,

    @NonNull
    @ColumnInfo(name = "eid")
    val eid: Int,

    @NonNull
    @ColumnInfo(name = "date")
    val date: String,

    @NonNull
    @ColumnInfo(name = "type")
    val title: String,

    @NonNull
    @ColumnInfo(name = "image_url")
    val image_url: String,

    @NonNull
    @ColumnInfo(name = "amount")
    val amount: Float,

    @NonNull
    @ColumnInfo(name = "reason")
    val reason: String,

    @NonNull
    @ColumnInfo(name = "status")
    val status: String
)