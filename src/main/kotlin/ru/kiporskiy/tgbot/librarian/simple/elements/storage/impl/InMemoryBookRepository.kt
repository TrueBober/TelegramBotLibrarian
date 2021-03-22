package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import ru.kiporskiy.tgbot.librarian.simple.elements.Book
import ru.kiporskiy.tgbot.librarian.simple.elements.storage.BookRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

/**
 * Хранилище для книг в памяти
 */
object InMemoryBookRepository : BookRepository {


    private val books: MutableSet<Book> = ConcurrentHashMap.newKeySet()


    override fun findAll() = books.toList()

    override fun findAllOrderByTitlePageable(pageNumber: Int, pageSize: Int): List<Book> {
        val sortedList = books.sortedBy { it.title }
        return getPageable(sortedList, pageNumber, pageSize)
    }

    override fun findWithCategoryOrderByTitlePageable(category: Int, pageNumber: Int, pageSize: Int): List<Book> {
        val sortedList = find(category).sortedBy { it.title }
        return getPageable(sortedList, pageNumber, pageSize)
    }

    private fun getPageable(list: List<Book>, page: Int, pageSize: Int): List<Book> {
        val offset = page * pageSize
        val limit = min(pageSize, books.size - offset)
        return list.subList(offset, offset + limit)
    }

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
