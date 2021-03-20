package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import ru.kiporskiy.tgbot.librarian.simple.CATEGORY_BOOK_MAX_DEEP
import ru.kiporskiy.tgbot.librarian.simple.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.simple.elements.storage.BookCategoryRepository
import ru.kiporskiy.tgbot.librarian.simple.exception.BookCategoryAlreadyExistsException
import ru.kiporskiy.tgbot.librarian.simple.exception.BookCategoryLoopException
import ru.kiporskiy.tgbot.librarian.simple.exception.BookCategoryTooDeepNestingExistsException
import ru.kiporskiy.tgbot.librarian.simple.exception.CategoryNotFoundException
import java.util.concurrent.ConcurrentHashMap.newKeySet
import java.util.concurrent.atomic.AtomicInteger

/**
 * Реализация хранилища категорий книг по умолчанию - в оперативной памяти
 */
object InMemoryBookCategoryRepository : BookCategoryRepository {
    private val categoryIdCounter = AtomicInteger(1)

    /**
     * Хранилище всех категорий книг
     */
    private val allCategories: MutableSet<BookCategory> = newKeySet()


    override fun findAll() = allCategories.toList()

    override fun findById(id: Int) = allCategories.firstOrNull { it.id == id }

    override fun findByName(name: String) = allCategories.firstOrNull { it.name == name }

    override fun add(name: String, parent: BookCategory?): BookCategory {
        //найти категорию с таким же названием
        val existsCategory = findByName(name)

        if (existsCategory != null && parent == existsCategory.parent)
        //если категория с таким именем уже была добавлена раньше - вернуть ее
            return existsCategory
        else if (existsCategory != null)
        //если категория с таким именем уже была добавлена раньше, но у категорий не совпадает
        // "родитель" - исключение
            throw BookCategoryAlreadyExistsException(existsCategory)

        val newCategory = createNewCategory(name, parent)

        //проверка цикличности категорий
        checkCategoryLoop(newCategory)

        allCategories += newCategory

        return newCategory
    }

    private fun createNewCategory(name: String, parent: BookCategory?): BookCategory {
        val id = categoryIdCounter.incrementAndGet()
        return BookCategory(id, name, parent)
    }

    private fun checkCategoryLoop(category: BookCategory) {
        val parents = HashSet<BookCategory>()
        var currentCategory: BookCategory? = category

        while (currentCategory != null) {
            if (parents.size > CATEGORY_BOOK_MAX_DEEP)
                throw BookCategoryTooDeepNestingExistsException(category)

            if (parents.contains(currentCategory))
                throw BookCategoryLoopException(category)

            parents += currentCategory
            currentCategory = currentCategory.parent
        }
    }

    override fun updateName(src: BookCategory, name: String) {
        if (src.name == name)
            return

        //найти категорию с таким же названием
        val existsCategory = findByName(name)
        if (existsCategory != null)
            throw BookCategoryAlreadyExistsException(existsCategory)

        if (allCategories.contains(src))
        //обновляемая категория не найдена
            throw CategoryNotFoundException(src)

        src.name = name
    }

    override fun updateParent(src: BookCategory, parent: BookCategory?) {
        if (src.parent == parent)
            return

        if (!allCategories.contains(src))
        //обновляемая категория не найдена
            throw CategoryNotFoundException(src)

        if (parent != null && !allCategories.contains(parent))
        //родительская категория не найдена
            throw CategoryNotFoundException(parent)

        //обновить категорию
        val updatedCategory = BookCategory(src.id, src.name, parent)

        checkCategoryLoop(updatedCategory)

        src.parent = parent
    }

    /**
     * Удалить все категории
     */
    internal fun reset() {
        allCategories.clear()
    }
}
