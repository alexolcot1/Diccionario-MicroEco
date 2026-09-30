package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MicroDiccionario", appName)
  }

  @Test
  fun `verify syllabus terms loaded`() {
    val terms = com.example.data.local.PreloadedTerms.list
    org.junit.Assert.assertTrue(terms.isNotEmpty())
    org.junit.Assert.assertTrue(terms.any { it.term == "Microeconomía" })
    org.junit.Assert.assertTrue(terms.any { it.term.contains("Tasa Marginal de Sustitución") })
    org.junit.Assert.assertTrue(terms.any { it.term.contains("Elasticidad") })
    org.junit.Assert.assertTrue(terms.any { it.term == "Competencia Perfecta" })
  }
}
