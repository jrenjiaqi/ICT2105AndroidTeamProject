package com.example.ict2105project.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ict2105project.entity.Role

@Dao
interface RoleDAO {
    /** (TESTED AND WORKING)
     * function to get employee role details by rid
     *
     * @param rid a role id of current logged in employee
     * @return String containing a role name get by rid
     */
    @Query("SELECT name FROM role WHERE role.rid LIKE :rid")
    fun getEmployeeRole(rid: Int): String

    /** (TESTED AND WORKING)
     * function to get all role details
     *
     * @return List<Role> containing list of role id and role name
     */
    @Query("SELECT * FROM role")
    fun getAllRole(): List<Role>
}