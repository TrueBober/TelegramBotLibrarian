package ru.kiporskiy.tgbot.librarian.simple.elements

/**
 * Категории книг
 * Категории имеют древовидную структуру. Уникальность задается полем name
 *
 * @param name уникальное название категории в рамках всей библиотеки
 * @param parent родительская категория. Может отсутствовать, тогда будет назначена категория по умолчанию.
 */
data class BookCategory(val name: String, val parent: BookCategory? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookCategory

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun hasParent() = parent != null
}
