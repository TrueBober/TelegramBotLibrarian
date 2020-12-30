package ru.kiporskiy.tgbot.librarian

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test

internal class BookCategoryTest {

    @Test
    internal fun checkParentCategory() {
        val categoryA = getCategory("A")
        val categoryA1 = getCategory("A1", categoryA)
        val categoryB = getCategory("B")
        val categoryB1 = getCategory("B1", categoryB)

        assertTrue(categoryA.isParentFor(categoryA1))
        assertTrue(categoryB.isParentFor(categoryB1))
        assertFalse(categoryB1.isParentFor(categoryB))
        assertFalse(categoryB1.isParentFor(categoryA))
        assertFalse(categoryA.isParentFor(categoryB))
        assertFalse(categoryB.isParentFor(categoryA))
    }
}
