package com.bedrocklauncherx

import com.bedrocklauncherx.data.WorldValidation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WorldValidationTest {
    @Test fun rejectsBlankNames() {
        assertNull(WorldValidation.normalizedName("   "))
    }

    @Test fun trimsWhitespaceFromName() {
        assertEquals("Survival", WorldValidation.normalizedName("  Survival  "))
    }
}
