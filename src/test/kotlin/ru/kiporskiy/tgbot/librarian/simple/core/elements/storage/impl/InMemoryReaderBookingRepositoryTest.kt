package ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.impl

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.kiporskiy.tgbot.librarian.getTestBook
import ru.kiporskiy.tgbot.librarian.getTestUser
import ru.kiporskiy.tgbot.librarian.simple.core.elements.Book
import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.simple.core.elements.User
import ru.kiporskiy.tgbot.librarian.simple.core.exception.BookAlreadyBookingException
import kotlin.test.assertEquals

internal class InMemoryReaderBookingRepositoryTest {

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var reader1: Reader
    private lateinit var reader2: Reader
    private lateinit var book1: Book
    private lateinit var book2: Book

    @BeforeEach
    internal fun setUp() {
        InMemoryReaderBookingRepository.clear()
        user1 = getTestUser()
        user2 = getTestUser()
        reader1 = InMemoryReaderRepository.getReader(user1)
        reader2 = InMemoryReaderRepository.getReader(user2)
        book1 = getTestBook("1")
        book2 = getTestBook("2")
        InMemoryBookRepository.add(book1)
        InMemoryBookRepository.add(book2)
    }

    @Test
    @DisplayName("Тест для проверки бронирования книг")
    internal fun booking() {
        InMemoryReaderBookingRepository.booking(reader1, book1)
        assertThrows<BookAlreadyBookingException> { InMemoryReaderBookingRepository.booking(reader2, book1) }

        val booksReader1 = InMemoryReaderBookingRepository.getReaderBooking(reader1)
        val booksReader2 = InMemoryReaderBookingRepository.getReaderBooking(reader2)
        val readerBook1 = InMemoryReaderBookingRepository.getBookBooking(book1)
        val readerBook2 = InMemoryReaderBookingRepository.getBookBooking(book2)

        assertEquals(listOf(book1), booksReader1)
        assertEquals(listOf(), booksReader2)
        assertEquals(reader1, readerBook1)
        assertNull(readerBook2)
    }
}
