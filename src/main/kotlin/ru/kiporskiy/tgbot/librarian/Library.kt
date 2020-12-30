package ru.kiporskiy.tgbot.librarian

import java.util.concurrent.ConcurrentSkipListMap

/**
 * Книжная полка. Хранит набор книг
 */
class Bookshelf {

    /**
     * Отображение ISBN книги на саму книгу.
     * Хранит все доступные для бронирования книги
     */
    private val books: MutableMap<String, Book> = ConcurrentSkipListMap()

    /**
     * Получить состояние книги по ISBN
     */
    fun getBook(isbn: String) = books[isbn]

    /**
     * Получить состояние книги
     */
    fun getBook(book: Book) = this.getBook(book.isbn)

    /**
     * Получить список всех доступных книг
     */
    fun getBooks() = listOf(this.books.values)

    /**
     * Получить список всех доступных книг для категории и всех ее потомков
     */
    fun getBooks(category: BookCategory): List<Book> {
        return books.values.filter { category.isParentFor(it.category) }
    }

    /**
     * Добавить новую книгу на полку.
     * При добавлении книги, которая уже забронирована будет выкинуто исключение IllegalArgumentException
     */
    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    /**
     * Вернуть забронированную книгу на полку.
     * Вернуть можно только ту книгу, которая уже была добавлена, а потом забронирована
     */
    fun returnBook(book: Book) {
        this.addBook(book)
    }

    /**
     * Забронировать книгу.
     * Забронировать можно только свободную книгу.
     *
     * @throws IllegalStateException попытка забронировать книгу, которая не находится на полке
     */
    fun booking(book: Book) {
        books.remove(book.isbn) ?: throw IllegalStateException()
    }
}
