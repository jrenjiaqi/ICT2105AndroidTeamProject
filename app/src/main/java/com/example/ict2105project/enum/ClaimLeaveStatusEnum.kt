package com.example.ict2105project.enum

/**
 * An enum to standardise the representation for approval status for Claims and Leaves
 * @param status: the string representation for the approval statuses for claims and leaves
 */
enum class ClaimLeaveStatusEnum (val status: String) {
    // Approved claims/leave application string representation.
    APPROVED("Approved"),
    // Rejected claims/leave application string representation.
    REJECTED("Rejected"),
    // Pending claims/leave application string representation.
    PENDING("Pending")
}