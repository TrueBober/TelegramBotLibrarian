package ru.kiporskiy.tgbot.librarian.simple.handle.command

import ru.kiporskiy.tgbot.librarian.simple.handle.request.ReaderRequest

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
