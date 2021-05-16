package ru.kiporskiy.tgbot.librarian.handle.request.impl

import ru.kiporskiy.tgbot.librarian.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.handle.request.AbstractReaderRequest

/**
 * Неизвестный запрос
 */
object UnknownReaderRequest : AbstractReaderRequest() {

    private const val description = "неизвестная команда"

    override fun getCommand() = ""

    override fun getDescription() = description

    //доступно всем пользователям
    override fun getAccessRole() = ReaderRole.USER
}
