package com.example.ict2105project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ict2105project.dao.*
import com.example.ict2105project.entity.*

@Database(entities = [Employee::class,
                        Role::class,
                        AttendanceRecord::class,
                        LeaveRecord::class,
                        ClaimRecord::class],
                        version = 4, exportSchema = true)
abstract class HRDB : RoomDatabase() {
    abstract fun EmployeeDAO(): EmployeeDAO
    abstract fun RoleDAO(): RoleDAO
    abstract fun AttendanceRecordDAO(): AttendanceRecordDAO
    abstract fun ClaimRecordDAO(): ClaimRecordDAO
    abstract fun LeaveRecordDAO(): LeaveRecordDAO


    //Singleton pattern for database instance
    companion object {
        @Volatile
        private var INSTANCE: HRDB? = null

        fun getDatabase(context: Context): HRDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HRDB::class.java,
                    "hr_db"
                ).createFromAsset("database/hrdatabase.db").build()

                INSTANCE = instance
                instance
            }
        }
    }
}