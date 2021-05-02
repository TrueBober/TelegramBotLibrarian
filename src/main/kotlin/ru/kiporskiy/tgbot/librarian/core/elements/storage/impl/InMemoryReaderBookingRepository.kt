package ru.kiporskiy.tgbot.librarian.core.elements.storage.impl

import ru.kiporskiy.tgbot.librarian.core.elements.Book
import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.storage.BookRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.ReaderBookRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.ReaderRepository
import ru.kiporskiy.tgbot.librarian.core.exception.BookAlreadyBookingException
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.util.*

/**
 * Реализация хранилища читателей в оперативной памяти.
 * Потокобезопасный объект
 */
object InMemoryReaderBookingRepository : ReaderBookRepository {

    /**
     * Репозиторий с книгами
     */
    private val bookRepository: BookRepository = InMemoryBookRepository

    /**
     * Репозиторий с читателями
     */
    private val readerRepository: ReaderRepository = InMemoryReaderRepository

    /**
     * Забронированные пользователем книги
     */
    private val booking = LinkedList<Booking>()

    @Synchronized
    override fun getReaderBooking(reader: Reader): List<Book> {
        return booking.asSequence()
            .filter { it.readerId == reader.id }
            .map { it.bookIsbn }
            .map { bookRepository.find(it) ?: throw IllegalStateException("Book with isbn $it") }
            .toList()
    }

    @Synchronized
    override fun getBookBooking(book: Book): Reader? {
        return booking.asSequence()
            .filter { it.bookIsbn == book.isbn }
            .map { it.readerId }
            .map { readerRepository.getReader(it) ?: throw IllegalStateException("Reader with id $it") }
            .firstOrNull()
    }

    @Synchronized
    override fun booking(reader: Reader, book: Book) {
        //книга может быть забронирована только одним пользователем
        if (getBookBooking(book) != null)
            throw BookAlreadyBookingException

        //сохранить инфу о бронировании книги
        val booking = Booking(reader.id, book.isbn, LocalDateTime.now())
        this.booking.add(booking)
    }

    /**
     * Хранилище для забронированных книг
     *
     * @param readerId ид читателя
     * @param bookIsbn ид книги
     * @param date дата бронирования
     */
    private data class Booking(val readerId: Long, val bookIsbn: String, val date: LocalDateTime)

    internal fun clear() {
        booking.clear()
    }
}
