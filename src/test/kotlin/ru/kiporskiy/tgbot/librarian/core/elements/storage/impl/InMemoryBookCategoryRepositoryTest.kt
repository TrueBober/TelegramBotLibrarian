package ru.kiporskiy.tgbot.librarian.core.elements.storage.impl

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.kiporskiy.tgbot.librarian.core.CATEGORY_BOOK_MAX_DEEP
import ru.kiporskiy.tgbot.librarian.core.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.core.exception.BookCategoryAlreadyExistsException
import ru.kiporskiy.tgbot.librarian.core.exception.BookCategoryLoopException
import ru.kiporskiy.tgbot.librarian.core.exception.BookCategoryTooDeepNestingExistsException
import ru.kiporskiy.tgbot.librarian.core.exception.CategoryNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertSame
import ru.kiporskiy.tgbot.librarian.core.elements.storage.impl.InMemoryBookCategoryRepository as repo

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
    internal fun updateParent_categoryLoop() {
        val categoryA = repo.add("A", null)
        val categoryB = repo.add("B", categoryA)
        val categoryC = repo.add("C", null)

        repo.updateParent(categoryA, categoryC)
        assertThrows<BookCategoryLoopException> { repo.updateParent(categoryC, categoryB) }
    }

    @Test
    @DisplayName("Ошибка при изменении родительской категории. Исходная категория не найдена")
    internal fun updateParent_srcCategoryNotFound() {
        val category = BookCategory(0, "")
        val categoryA = repo.add("A", null)

        assertThrows<CategoryNotFoundException> { repo.updateParent(category, categoryA) }
    }

    @Test
    @DisplayName("Ошибка при изменении родительской категории. Целевая категория не найдена")
    internal fun updateParent_targetCategoryNotFound() {
        val category = BookCategory(0, "")
        val categoryA = repo.add("A", null)

        assertThrows<CategoryNotFoundException> { repo.updateParent(categoryA, category) }
    }

    @Test
    @DisplayName("Смена названия категории")
    internal fun updateName_ok() {
        val categoryName = "Test"
        val category = repo.add("A", null)
        repo.updateName(category, categoryName)

        val resultCategory = repo.findById(category.id)
        assertEquals(categoryName, resultCategory!!.name)
    }

    @Test
    @DisplayName("Смена названия категории запрещена, т.к. категория с таким названием уже есть")
    internal fun updateName_alreadyExists() {
        val categoryA = repo.add("A", null)
        val categoryB = repo.add("B", null)
        assertThrows<BookCategoryAlreadyExistsException> {
            repo.updateName(categoryA, categoryB.name)
        }
    }

    @Test
    @DisplayName("Категория для смены названия не найдена")
    internal fun updateName_notFound() {
        val category = BookCategory(0, "")
        assertThrows<CategoryNotFoundException> {
            repo.updateName(category, "Test")
        }
    }
}
