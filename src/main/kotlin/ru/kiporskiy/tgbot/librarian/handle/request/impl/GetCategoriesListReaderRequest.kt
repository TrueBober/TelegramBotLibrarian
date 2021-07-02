package ru.kiporskiy.tgbot.librarian.handle.request.impl

import ru.kiporskiy.tgbot.librarian.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.handle.request.AbstractReaderRequest

/**
 * Запрос получения списка доступных команд
 */
object GetCategoriesListReaderRequest : AbstractReaderRequest() {

    private const val command = "/get_categories"

    private const val description = "получить список категорий"

    override fun getCommand() = command

    override fun getDescription() = description

    //доступно всем пользователям
    override fun getAccessRole() = ReaderRole.USER
}
