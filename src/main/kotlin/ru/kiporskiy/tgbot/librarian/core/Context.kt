package ru.kiporskiy.tgbot.librarian.core

import ru.kiporskiy.tgbot.librarian.handle.command.Command
import ru.kiporskiy.tgbot.librarian.handle.command.ContextCommand

/**
 * Интерфейс контекста
 * Контекст нужен для сохранения последовательности действий на предыдущих шагах. Например, при добавлении новой книги
 * админ на первом шаге добавляет автора, на втором шаге - название книги и т.д., контекст сохраняет всё, что вводит
 * пользователь.
 */
interface Context {

    val command: ContextCommand?

    /**
     * Установить контекстное сообщение
     */
    fun setContextMessage(message: String)

}

/**
 * Пустой контекст без содержимого
 */
object EmptyContext: Context {

    override val command: ContextCommand?
        get() = null

    override fun setContextMessage(message: String) {
        throw UnsupportedOperationException()
    }
}

/**
 * Контекст запроса при добавлении категории
 */
data class BookCategoryContext(var categoryName: String,
                               override val command: ContextCommand): Context {

    override fun setContextMessage(message: String) {
        this.categoryName = message
    }
}
