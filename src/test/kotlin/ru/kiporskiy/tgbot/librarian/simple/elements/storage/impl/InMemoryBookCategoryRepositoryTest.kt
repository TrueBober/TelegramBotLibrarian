package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.kiporskiy.tgbot.librarian.simple.CATEGORY_BOOK_MAX_DEEP
import ru.kiporskiy.tgbot.librarian.simple.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.simple.exception.BookCategoryAlreadyExistsException
import ru.kiporskiy.tgbot.librarian.simple.exception.BookCategoryLoopException
import ru.kiporskiy.tgbot.librarian.simple.exception.BookCategoryTooDeepNestingExistsException
import kotlin.test.assertEquals
import kotlin.test.assertSame
import ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl.InMemoryBookCategoryRepository as repo

internal class InMemoryBookCategoryRepositoryTest {

    @BeforeEach
    internal fun setUp() {
        repo.reset()
        assertTrue(repo.findAll().isEmpty())
    }

    @Test
    @DisplayName("Проверяется успешное добавление категории")
    internal fun addCategory() {
        val name = "cat_name"
        val result = repo.add(name, null)
        assertEquals(result.name, name)
    }

    @Test
    @DisplayName("Проверяется успешное извлечение категории по имени")
    internal fun getCategoryByName() {
        val name = "cat_name"
        val result = repo.add(name, null)
        val found = repo.findByName(name)
        assertSame(result, found)
    }

    @Test
    @DisplayName("Проверяется успешное извлечение категории по ид")
    internal fun getCategoryById() {
        val name = "cat_name"
        val result = repo.add(name, null)
        val found = repo.findById(result.id)
        assertSame(result, found)
    }

    @Test
    @DisplayName("Проверяется, что при добавлении категории с тем же именем вернется старая категория")
    internal fun addCategory_alreadyExists() {
        val name = "cat_name"
        val result1 = repo.add(name, null)
        val result2 = repo.add(name, null)
        assertEquals(result1, result2)
    }

    @Test
    @DisplayName("Ошибка при добавлении категории с тем же именем, но другим родителем")
    internal fun addCategory_alreadyExistsWithOtherParent() {
        val name = "cat_name"
        val parentName = "parent_name"
        val parent = repo.add(parentName, null)
        repo.add(name, null)
        assertThrows<BookCategoryAlreadyExistsException> { repo.add(name, parent) }
    }

    @Test
    @DisplayName("Ошибка при слишком большом количестве \"родителей\"")
    internal fun addCategory_tooDeepParents() {
        var parent: BookCategory? = null

        for (i in 0 .. CATEGORY_BOOK_MAX_DEEP) {
            val name = "cat_name_$i"
            val category = repo.add(name, parent)
            parent = category
        }

        val name = "last"
        assertThrows<BookCategoryTooDeepNestingExistsException> { repo.add(name, parent) }
    }

    @Test
    @DisplayName("Ошибка при добавлении циклических зависимостей в репозиториях")
    internal fun addCategory_categoryLoop() {
        val categoryA = repo.add("A", null)
        val categoryB = repo.add("B", categoryA)
        val categoryC = repo.add("C", null)

        repo.updateParent(categoryA, categoryC)
        assertThrows<BookCategoryLoopException> { repo.updateParent(categoryC, categoryB) }
    }
}
