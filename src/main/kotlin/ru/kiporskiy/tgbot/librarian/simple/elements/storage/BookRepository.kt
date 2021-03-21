package ru.kiporskiy.tgbot.librarian.simple.elements.storage

import ru.kiporskiy.tgbot.librarian.simple.elements.Book
import ru.kiporskiy.tgbot.librarian.simple.elements.BookCategory

/**
 * Репозиторий для получения книг
 */
interface BookRepository {

    /**
     * Найти все книги
     */
    fun findAll(): List<Book>

    /**
     * Поиск книги по ISBN
     */
    fun find(isbn: String): Book?

    /**
     * Поиск книг по категории
     */
    fun find(category: Int): List<Book>

    /**
     * Добавить новую книгу
     */
    fun add(book: Book)
}
