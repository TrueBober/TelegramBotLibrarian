package ru.kiporskiy.tgbot.librarian

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Книга - уникальный в рамках системы объект
 *
 * Немутируемый класс. Содержит уникальный номер ISBN (он же служит для вычисления hash-кода объекта и проверки
 * уникальности). Каждая книга относится к определенной категории. Если книга не подошла ни к одной категории ей
 * присваивается категория по-умолчанию UnclassifiedCategory.
 */
data class Book(
    val title: String,
    val author: String,
    val createDate: LocalDateTime,
    val numberSheets: Short,
    val isbn: String,
    val description: String = "",
    val category: BookCategory = BookCategory.getUnclassifiedCategory(),
    val publicationDate: LocalDate? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (isbn != other.isbn) return false

        return true
    }

    override fun hashCode(): Int {
        return isbn.hashCode()
    }
}

/**
 * Категории книг.
 *
 * Немутируемый класс. Если книга не относится ни к одной известной категории, то по умолчанию она получает
 * NO_CATEGORY категорию. Категории имеют древовидную структуру. Уникальность категории определяется ее названием.
 */
data class BookCategory(val name: String) {

    companion object {
        private val NO_CATEGORY = BookCategory("")
        fun getUnclassifiedCategory() = NO_CATEGORY
    }

    var parent: BookCategory? = null
        private set

    constructor(name: String, parent: BookCategory) : this(name) {
        this.parent = parent
    }

}
