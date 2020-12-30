package ru.kiporskiy.tgbot.librarian

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BookshelfTest {

    lateinit var bookshelf: Bookshelf

    @BeforeEach
    internal fun setUp() {
        bookshelf = Bookshelf()
    }

    @Test
    internal fun addAndGetBooks() {
        val categoryA = getCategory("A")
        val categoryA1 = getCategory("A1", categoryA)
        val categoryA2 = getCategory("A2", categoryA1)
        val book1 = getBook("1", categoryA2)
        val book2 = getBook("2")
        bookshelf.addBook(book1)
        bookshelf.addBook(book2)

        val result1 = bookshelf.getBook("1")
        val result2 = bookshelf.getBook("2")

        assertSame(book1, result1)
        assertSame(book2, result2)
        assertEquals(listOf(book1, book2), bookshelf.getBooks())
        assertEquals(listOf(book1, book2), bookshelf.getBooks(BookCategory.getUnclassifiedCategory()))
        assertEquals(listOf(book1), bookshelf.getBooks(categoryA))
        assertEquals(listOf(book1), bookshelf.getBooks(categoryA1))
        assertEquals(listOf(book1), bookshelf.getBooks(categoryA2))
    }

    @Test
    internal fun booking() {
        val book1 = getBook("1")
        bookshelf.addBook(book1)

        bookshelf.booking(book1)

        assertTrue(bookshelf.getBooks().isEmpty())

        assertThrows(IllegalStateException::class.java) {
            bookshelf.booking(book1)
        }
    }
}
