package com.example.ict2105project

import android.app.Application
import com.example.ict2105project.database.HRDB
import com.example.ict2105project.repository.HRRepository

/**
 * used for setting up context so that they could be used
 */

class HRApp : Application() {
    val db by lazy { HRDB.getDatabase(this) }
    val repo by lazy { HRRepository(db.EmployeeDAO(), db.RoleDAO(),
                                    db.AttendanceRecordDAO(), db.ClaimRecordDAO(),
                                    db.LeaveRecordDAO()) }
}