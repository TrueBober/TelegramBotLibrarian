package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.Context
import ru.kiporskiy.tgbot.librarian.handle.request.ReaderRequest

/**
 * Команда для выполнения
 */
interface Command {

    /**
     * Выполнить команду
     */
    fun execute()

    /**
     * Описание запроса, для которого предназначена команда
     * Может быть null, если команда не предназначена для обработки конкретного запроса
     */
    fun getRequest(): ReaderRequest?
}

/**
 * Команда для выполнения с контекстом
 */
interface ContextCommand: Command {

    /**
     * Выполнить команду
     */
    fun execute(context: Context)

}
