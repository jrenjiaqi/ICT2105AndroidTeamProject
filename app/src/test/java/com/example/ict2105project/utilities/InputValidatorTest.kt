package com.example.ict2105project.utilities

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `empty claims title returns false`() {
        val result = InputValidator.isValidClaimsType("")
        assertThat(result).isFalse()
    }

    @Test
    fun `blank claims title returns false`() {
        val result = InputValidator.isValidClaimsType(" ")
        assertThat(result).isFalse()
    }

    @Test
    fun `date not using - delimiter returns false`() {
        val result = InputValidator.isValidDateFormat("12/12/2020")
        assertThat(result).isFalse()
    }

    @Test
    fun `date format with invalid day (higher) returns false`() {
        val result = InputValidator.isValidDateFormat("32-12-2020")
        assertThat(result).isFalse()
    }

    @Test
    fun `date format with invalid day (lower) returns false`() {
        val result = InputValidator.isValidDateFormat("00-12-2020")
        assertThat(result).isFalse()
    }

    @Test
    fun `date format with invalid month (higher) returns false`() {
        val result = InputValidator.isValidDateFormat("12-13-2020")
        assertThat(result).isFalse()
    }

    @Test
    fun `date format with invalid month (lower) returns false`() {
        val result = InputValidator.isValidDateFormat("12-00-2020")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty leaves reason returns false`() {
        val result = InputValidator.isValidLeavesReason("")
        assertThat(result).isFalse()
    }

    @Test
    fun `blank leaves reason returns false`() {
        val result = InputValidator.isValidLeavesReason(" ")
        assertThat(result).isFalse()
    }

    @Test
    fun `claims amount containing comma returns false`() {
        val result = InputValidator.isValidClaimsAmount("200,00")
        assertThat(result).isFalse()
    }

    @Test
    fun `claims amount containing dollar sign returns false`() {
        val result = InputValidator.isValidClaimsAmount("$20000")
        assertThat(result).isFalse()
    }

    @Test
    fun `claims amount containing period returns true`() {
        val result = InputValidator.isValidClaimsAmount("200.00")
        assertThat(result).isTrue()
    }

    @Test
    fun `claims amount containing no period returns true`() {
        val result = InputValidator.isValidClaimsAmount("20000")
        assertThat(result).isTrue()
    }

    @Test
    fun `claims image link without http protocol specifier returns false`() {
        val result = InputValidator.isValidClaimsImageLink("www.google.com")
        assertThat(result).isFalse()
    }

    @Test
    fun `claims image link without www specifier returns false`() {
        val result = InputValidator.isValidClaimsImageLink("http://google.com")
        assertThat(result).isFalse()
    }

    @Test
    fun `claims image link without com specifier returns false`() {
        val result = InputValidator.isValidClaimsImageLink("http://www.google")
        assertThat(result).isFalse()
    }

    @Test
    fun `claims image link with https returns true`() {
        val result = InputValidator.isValidClaimsImageLink("https://www.google.com")
        assertThat(result).isTrue()
    }

    @Test
    fun `claims image link without https returns false`() {
        val result = InputValidator.isValidClaimsImageLink("http://www.google.com")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime not using - delimiter for date returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12/12/2020 00:00:00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime not using colon delimiter for time returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12-12-2020 00-00-00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime format with invalid day (higher) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("32-12-2020 00:00:00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime format with invalid day (lower) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("00-12-2020 00:00:00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime format with invalid month (higher) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12-13-2020 00:00:00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime format with invalid month (lower) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12-00-2020 00:00:00")
        assertThat(result).isFalse()
    }

    @Test
    // Note 24:00:00 is a valid hour
    fun `datetime format with invalid hour (higher) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12-12-2020 25:00:00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime format with invalid minutes (higher) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12-12-2020 00:61:00")
        assertThat(result).isFalse()
    }

    @Test
    fun `datetime format with invalid seconds (higher) returns false`() {
        val result = InputValidator.isValidDateTimeFormat("12-12-2020 00:00:61")
        assertThat(result).isFalse()
    }


    @Test
    fun `For isValidClockOut, clockout after 6pm returns true`() {
        val result = InputValidator.isValidClockOut("18:00:00")
        assertThat(result).isTrue()
    }

    @Test
    fun `For isClockInLate, clockin after 9am returns true`() {
        val result = InputValidator.isClockInLate("09:00:01")
        assertThat(result).isTrue()
    }
}