package ru.kiporskiy.tgbot.librarian.handle.request

import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.ReaderRole

/**
 * Запросы пользователей
 */
interface ReaderRequest {

    /**
     * Требует ли запрос какого-то контекста пользователя
     */
    fun isWithContext(): Boolean

    /**
     * Получить команду для запроса
     * Пример: /get_books
     */
    fun getCommand(): String

    /**
     * Содержит ли текст указанную команду
     */
    fun isCommand(command: String): Boolean

    /**
     * Получить описание запроса
     * Пример: Получение списка книг
     */
    fun getDescription(): String

    /**
     * Требуемая для доступа к запросу роль читателя
     */
    fun getAccessRole(): ReaderRole

    /**
     * Имеет ли пользователь доступ к выполнению запроса
     */
    fun hasAccess(reader: Reader): Boolean

}


abstract class AbstractReaderRequest: ReaderRequest {

    //Читатель имеет доступ к функции, когда его роль подходит для этой функции
    override fun hasAccess(reader: Reader) = getAccessRole().ordinal <= reader.role.ordinal

    //Большинству запросов не нужен контекст
    override fun isWithContext() = false

    override fun isCommand(command: String) = command.startsWith(getCommand())

}
