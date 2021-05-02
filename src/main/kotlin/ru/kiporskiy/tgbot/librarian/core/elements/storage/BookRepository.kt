package ru.kiporskiy.tgbot.librarian.core.elements.storage

import ru.kiporskiy.tgbot.librarian.core.elements.Book

/**
 * Репозиторий для получения книг
 */
interface BookRepository {

    /**
     * Найти все книги
     */
    fun findAll(): List<Book>

    /**
     * Постраничный поиск книг с сортировкой по названию
     */
    fun findAllOrderByTitlePageable(pageNumber: Int, pageSize: Int): List<Book>

    /**
     * Постраничный поиск книг определенной категории с сортировкой по названию
     */
    fun findWithCategoryOrderByTitlePageable(category: Int, pageNumber: Int, pageSize: Int): List<Book>

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
