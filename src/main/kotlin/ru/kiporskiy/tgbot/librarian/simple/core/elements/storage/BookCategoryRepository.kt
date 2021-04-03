package ru.kiporskiy.tgbot.librarian.simple.core.elements.storage

import ru.kiporskiy.tgbot.librarian.simple.core.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.simple.core.exception.*

/**
 * Репозиторий для получения категорий книг
 */
interface BookCategoryRepository {

    /**
     * Найти все категории
     */
    fun findAll(): List<BookCategory>

    /**
     * Найти категорию по идентификатору
     */
    fun findById(id: Int): BookCategory?

    /**
     * Найти категорию по названию
     */
    fun findByName(name: String): BookCategory?

    /**
     * Добавить категорию
     * Название категории должно быть уникальным в рамках всего приложения. Не допускается циклических зависимостей
     * между категориями (Б - "родитель" для А, В - "родитель" для Б, а А - "родитель" для В)
     *
     * @throws BookCategoryAlreadyExistsException если в хранилище уже есть категория с таким названием
     * @throws BookCategoryTooDeepNestingExistsException при попытке создать слишком большую вложенность категорий
     * @throws BookCategoryLoopException при попытке создать категории с цикличной зависимостью
     */
    fun add(name: String, parent: BookCategory?): BookCategory

    /**
     * Обновить название категории
     *
     * @throws BookCategoryAlreadyExistsException если в хранилище уже есть категория с таким названием
     * @throws CategoryNotFoundException когда целевая категория не найдена
     */
    fun updateName(src: BookCategory, name: String)

    /**
     * Обновить родителя для категории
     *
     * @throws CategoryNotFoundException обновляемая или родительская категория не найдена
     */
    fun updateParent(src: BookCategory, parent: BookCategory?)

}
