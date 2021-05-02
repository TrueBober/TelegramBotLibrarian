package ru.kiporskiy.tgbot.librarian.core.elements

import ru.kiporskiy.tgbot.librarian.core.Context
import java.util.concurrent.ConcurrentHashMap

/**
 * Менеджер контекстов пользователей
 */
object ContextManager {

    /**
     * Контексты пользователей
     */
    private val readerContexts: MutableMap<Long, Context> = ConcurrentHashMap()

    /**
     * Получить контекст пользователя
     */
    fun getContext(reader: Reader): Context? = readerContexts[reader.id]

    /**
     * Получить контекст пользователя
     */
    fun setContext(reader: Reader, context: Context) {
        readerContexts[reader.id] = context
    }

    /**
     * Очистить контекст пользователя
     */
    fun clearContext(reader: Reader) {
        readerContexts.remove(reader.id)
    }
}
