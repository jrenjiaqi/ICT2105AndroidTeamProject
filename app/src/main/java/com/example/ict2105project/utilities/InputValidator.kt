package com.example.ict2105project.utilities

import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class InputValidator {
    companion object {
        private const val MAX_CHAR_COUNT_CLAIM_REASON = 80
        private const val MAX_CHAR_COUNT_LEAVE_REASON = 80
        /**
         * Claims & Leaves Function Validation
         * @param input any input string
         * @return Boolean indicating whether claims is valid
         */
        fun isValidClaimsType(input: String): Boolean {
            if (input.isEmpty()) {
                return false
            }
            if (input.isBlank()) {
                return false
            }
            return true
        }

        fun isValidClaimsAmount(input: String): Boolean {
            try {
                input.toFloat()
            } catch (e: NumberFormatException) {
                return false
            }
            return true
        }
        fun isValidClaimsImageLink(input: String): Boolean {
            if (!input.contains("https://")) {
                return false
            }
            return true
        }

        fun isValidClaimsReason(input: String): Boolean {
            if (input.isEmpty()) {
                return false
            }
            if (input.isBlank()) {
                return false
            }
            if (input.length > MAX_CHAR_COUNT_CLAIM_REASON) {
                return false
            }
            return true
        }

        /**
         * Converts single digit date (3-9-2021) to double digit dates (03-09-2021)
         * @param input any input string
         * @return String of converted date
         */
        private fun convertSingleDigitDateToDoubleDigitDate(input: String): String {
            val dateMonthYearAsStrList = input.split("-").toMutableList()
            // if day is single digit (e.g. 1 from 1st of Jan)
            if (dateMonthYearAsStrList[0].length < 2) {
                // append 0 in front of single digit day
                dateMonthYearAsStrList[0] = "0" + dateMonthYearAsStrList[0]
            }
            // if month is single digit (e.g. 2 from February)
            if (dateMonthYearAsStrList[1].length < 2) {
                // append 0 in front of single digit month
                dateMonthYearAsStrList[1] = "0" + dateMonthYearAsStrList[1]
            }
            // this is the new input with single digits made a lil' less lonely
            return dateMonthYearAsStrList.joinToString("-")
        }

        /**
         * checks if it's a valid date format
         * @param input any string input
         * @return Boolean of whether date is valid or not
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun isValidDateFormat(input: String): Boolean {
            if (!input.contains("-")) {
                return false
            }
            if (input.isBlank() || input.isEmpty()) {
                return false
            }
            try {
                val newInput = convertSingleDigitDateToDoubleDigitDate(input)
                LocalDate.parse(newInput, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            } catch (e: DateTimeParseException) {
                return false
            }
            return true
        }
        fun isValidLeavesReason(input: String): Boolean {
            if (input.isEmpty()) {
                return false
            }
            if (input.isBlank()) {
                return false
            }
            if (input.length > MAX_CHAR_COUNT_LEAVE_REASON) {
                return false
            }
            return true
        }

        /**
         * Checks if the input is a valid date and time format
         * @param input any input string
         * @return Boolean of whether input is in valid date time format
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun isValidDateTimeFormat(input: String): Boolean {
            try {
                LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
            } catch (e: DateTimeParseException) {
                return false
            }
            return true
        }

        /**
         * checks if clockin time is late
         * @param input any input string
         * @return Boolean of whether clockin is late or not
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun isClockInLate(input: String): Boolean {
            val clockInTimeStr = input.substringAfter(" ")
            val clockInTime = LocalTime.parse(clockInTimeStr)
            val onTime = LocalTime.parse("09:00:00")
            return clockInTime.isAfter(onTime)
        }

        /**
         * checks if clockout time is valid
         * @param input any input string
         * @return Boolean of whether clockout time is valid or not
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun isValidClockOut(input: String): Boolean {
            val clockOutTimeStr = input.substringAfter(" ")
            val clockOutTime = LocalTime.parse(clockOutTimeStr)
            val validClockOutTime = LocalTime.parse("17:59:59")
            return clockOutTime.isAfter(validClockOutTime)
        }

        /**
         * checks if employee id is valid or not
         * @param eidList, list of employee ids
         * @param input, any input integer
         * @return Boolean of whether the input int is an int inside eid list
         */
        fun ifEidIsValid (eidList : List<Int>, input: Int): Boolean {
            return eidList.contains(input)
        }

        /**
         * Login & Profile Function Validation
         *
         * namePattern:
         *      ^          -> starts of line
         *      [A-Z]{1}   -> any capital letter from A to Z; appear 1 time
         *      [a-z]+     -> any small letter from a to z; appear 1 or more times
         *      (?:\s[A-Z]{1}[a-z]+)*
         *          single space, any capital letter from A to Z appear 1 time,
         *          any small letter from a to z appear 1 or more times;
         *          capturing group that is optional to unlimited times
         *
         * emailPattern:
         *      ^               -> starts of line
         *      [A-Za-z0-9_-]+  -> any capital/small/digit/hyphen/underscore; appear 1 or more times
         *      @               -> single '@' character
         *      [A-Za-z]+       -> any capital/small letter from a to z; appear 1 or more times
         *      (?:\.[A-Za-z]+)*
         *          single dot, any capital/small letter from a to z appear 1 or more times,
         *          capturing group that is optional or unlimited times
         *      .               -> single '.' character
         *      (com|sg)        -> capturing group match exactly a single word "com" or "sg"
         *      $               -> end of line
         */
        private val namePattern: Regex = "^[A-Z]{1}[a-z]+(?:\\s[A-Z]{1}[a-z]+)*".toRegex()
        private val emailPattern: Regex = "^[A-Za-z0-9_-]+@[A-Za-z]+(?:\\.[A-Za-z]+)*\\.(com|sg)$".toRegex()
        fun isNameValid(input: String): Boolean {
            return !TextUtils.isEmpty(input) && namePattern.matches(input)
        }

        /**
         * function which checks if phone number is valid
         * @param input Any input of phone number
         * @return Boolean whether phone number is valid or not
         */
        fun isPhoneNumberValid(input: Any): Boolean {
            return !TextUtils.isEmpty(input.toString()) && Patterns.PHONE.matcher(input.toString()).matches()
        }

        /**
         * function which checks if email is valid
         * @param email string of email
         * @return Boolean of whether email is valid or not
         */
        fun isLoginEmailValid(email: String): Boolean {
            return !TextUtils.isEmpty(email) && emailPattern.matches(email)
        }
    }
}