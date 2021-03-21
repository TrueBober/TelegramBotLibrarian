package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import ru.kiporskiy.tgbot.librarian.simple.elements.Book
import ru.kiporskiy.tgbot.librarian.simple.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.simple.elements.storage.BookRepository
import java.util.concurrent.ConcurrentHashMap

/**
 * Хранилище для книг в памяти
 */
object InMemoryBookRepository : BookRepository {


    private val books: MutableSet<Book> = ConcurrentHashMap.newKeySet()


    override fun findAll() = books.toList()

    override fun find(isbn: String) = books.firstOrNull { it.isbn == isbn }

    override fun find(category: Int) = books.filter { it.category == category }

    override fun add(book: Book) {
        books.removeIf { it.isbn == book.isbn }
        books.add(book)
    }

    internal fun reset() {
        books.clear()
    }

}
