package ru.kiporskiy.tgbot.librarian.simple.elements

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class BookCategoryTest {

    /**
     * Создать новую категорию.
     *
     * Допустимо не указывать имя, тогда возьмется случайное имя
     * Допустимо не указывать родителя, тогда не будет родительской категории
     */
    private fun getCategory(
        name: String = Random.nextLong().toString(),
        parent: BookCategory? = null
    ) = BookCategory(name, parent)

    @Test
    @DisplayName("Проверка успешного создания экземпляра категории книг без родительской категории")
    internal fun createCategoryWithoutParent() {
        val result = getCategory()
        assertFalse(result.hasParent())
    }

    @Test
    @DisplayName("Проверка успешного создания экземпляра категории книг с родительской категорией")
    internal fun createCategoryWithParent() {
        val parent = getCategory()
        val result = getCategory(parent = parent)
        assertTrue(result.hasParent())
    }

    @Test
    @DisplayName("Проверить, что 2 категории с разными названиями не эквивалентны друг другу")
    internal fun createCategoryAndTestNotEquality() {
        val cat1 = getCategory()
        val cat2 = getCategory()
        assertNotEquals(cat1, cat2)
    }

    @Test
    @DisplayName("Проверить, что 2 категории с одинаковыми названиями эквивалентны друг другу")
    internal fun createCategoryAndTestEquality() {
        val parent1 = getCategory()
        val cat1 = getCategory("Parent1", parent1)
        val cat2 = getCategory("Parent1")
        assertEquals(cat1, cat2)
    }

}
