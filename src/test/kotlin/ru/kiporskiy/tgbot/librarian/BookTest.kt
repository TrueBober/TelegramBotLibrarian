package ru.kiporskiy.tgbot.librarian

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BookTest {

    @Test
    internal fun equivalence() {
        val bookA = getBook("1")
        val bookB = getBook("2")
        val bookC = getBook("2")
        assertFalse(bookA == bookB)
        assertTrue(bookB == bookC)
        assertFalse(bookC == bookA)
        print(bookA)
        print(bookB)
        print(bookC)
    }
}
