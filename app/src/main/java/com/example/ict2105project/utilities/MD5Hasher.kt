package com.example.ict2105project.utilities

import java.math.BigInteger
import java.security.MessageDigest


class MD5Hasher {
    companion object {
        /**
         * Function that takes in a string value and convert it into a MD5 hashed password
         * returns a string value of the MD5 hash
         */
        fun getEncodedStringValue(str: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(str.toByteArray())).toString(16).padStart(32, '0')
        }
    }
}