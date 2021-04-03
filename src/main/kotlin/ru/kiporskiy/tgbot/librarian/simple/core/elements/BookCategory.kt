package ru.kiporskiy.tgbot.librarian.simple.core.elements

/**
 * Категории книг
 * Категории имеют древовидную структуру. Уникальность задается идентификатором
 *
 * @param name уникальное название категории в рамках всей библиотеки
 * @param parent родительская категория. Может отсутствовать, тогда будет назначена категория по умолчанию.
 */
data class BookCategory(val id: Int, var name: String, var parent: BookCategory? = null) {

    fun hasParent() = parent != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookCategory

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
