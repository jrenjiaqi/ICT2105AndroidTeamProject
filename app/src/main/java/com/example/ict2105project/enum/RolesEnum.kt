package com.example.ict2105project.enum

/**
 * An enum that defines the role ID for the various employee positions.
 * @param id the role ID
 */
enum class RolesEnum(val id: Int) {
    STAFF(3),
    MANAGER(2),
    ADMIN(1)
}