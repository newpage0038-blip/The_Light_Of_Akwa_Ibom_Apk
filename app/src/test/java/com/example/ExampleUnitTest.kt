package com.example

import com.example.data.InitialData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun akwaIbom_has31Lgas() {
    assertEquals(31, InitialData.LGAS.size)
    assertTrue(InitialData.LGAS.contains("Uyo"))
    assertTrue(InitialData.LGAS.contains("Eket"))
    assertTrue(InitialData.LGAS.contains("Ikot Ekpene"))
    assertTrue(InitialData.LGAS.contains("Oron"))
    assertTrue(InitialData.LGAS.contains("Ikot Abasi"))
  }

  @Test
  fun initialData_containsEssentialContent() {
    assertFalse(InitialData.NEWS.isEmpty())
    assertFalse(InitialData.CULTURE.isEmpty())
    assertFalse(InitialData.OPPORTUNITIES.isEmpty())
    assertFalse(InitialData.BUSINESSES.isEmpty())
    assertFalse(InitialData.EVENTS.isEmpty())
    assertFalse(InitialData.DISCUSSIONS.isEmpty())
    assertFalse(InitialData.POLLS.isEmpty())

    // Verify Queen of Soups: Afang is present
    assertTrue(InitialData.CULTURE.any { it.title.contains("Afang", ignoreCase = true) })
    // Verify Ekombi dance is present
    assertTrue(InitialData.CULTURE.any { it.title.contains("Ekombi", ignoreCase = true) })
    // Verify Ibom Christmas Village is present
    assertTrue(InitialData.CULTURE.any { it.title.contains("Christmas", ignoreCase = true) })
  }
}
