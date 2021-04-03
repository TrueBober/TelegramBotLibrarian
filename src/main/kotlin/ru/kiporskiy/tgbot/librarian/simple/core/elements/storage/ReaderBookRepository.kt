package ru.kiporskiy.tgbot.librarian.simple.core.elements.storage

import ru.kiporskiy.tgbot.librarian.simple.core.elements.Book
import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.simple.core.exception.BookAlreadyBookingException

/**
 * Репозиторий для забронированных читателями книг
 */
interface ReaderBookRepository {

    /**
     * Получить список книг, забронированных читателем
     */
    fun getReaderBooking(reader: Reader): List<Book>

    /**
     * Получить пользователя, которым забронирована книга
     * Если книга свободна, то вернется null
     */
    fun getBookBooking(book: Book): Reader?

    /**
     * Забронировать книгу читателем
     *
     * @throws BookAlreadyBookingException если книга уже кем-то забронирована
     */
    fun booking(reader: Reader, book: Book)

}
