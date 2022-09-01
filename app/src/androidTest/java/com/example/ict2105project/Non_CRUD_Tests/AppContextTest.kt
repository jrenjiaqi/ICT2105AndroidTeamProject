@file:Suppress("IllegalIdentifier")
package com.example.ict2105project.Non_CRUD_Tests

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * Will be using Espresso:
 * https://developer.android.com/studio/test/other-testing-tools/espresso-test-recorder
 * https://developer.android.com/training/testing/espresso/setup
 *
 * NOTE: For Android Bumblebee, click on File > Settings > Build, Execution, Deployment >
 *                                       Testing > Uncheck "Run Android Instrumented Tests using Gradle"
 * Because it's not supported yet.
 *
 * NOTE: Backticked identifiers with spaces inside are errors in UI tests (even though it's not)...
 *       ... to work around this line 1 suppresses seeing spaces inside backticked identifiers as an error.
 */
@RunWith(AndroidJUnit4::class)
class AppContextTest {
    @Test
    fun appContextTest() {
        // Context of the app under test.
        val expectedContext = "com.example.ict2105project"
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(expectedContext, appContext.packageName)
    }

}