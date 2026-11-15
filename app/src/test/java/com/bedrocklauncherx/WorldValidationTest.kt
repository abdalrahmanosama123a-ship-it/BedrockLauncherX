package com.bedrocklauncherx

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WorldValidationTest {
    private fun isValidWorldName(value: String) = value.trim().isNotEmpty()

    @Test fun rejectsBlankNames() {
        assertFalse(isValidWorldName("   "))
    }

    @Test fun acceptsTrimmedWorldName() {
        assertTrue(isValidWorldName("  Survival  "))
    }
}
