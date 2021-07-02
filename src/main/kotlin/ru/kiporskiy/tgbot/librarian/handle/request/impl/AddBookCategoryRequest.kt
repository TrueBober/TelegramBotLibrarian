package ru.kiporskiy.tgbot.librarian.handle.request.impl

import ru.kiporskiy.tgbot.librarian.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.handle.request.AbstractReaderRequest

/**
 * Создание категории книг
 */
object AddBookCategoryRequest : AbstractReaderRequest() {

    private const val description = "добавить категорию книг"

    override fun getCommand() = "/add_category"

    override fun getDescription() = description

    //доступно только админам
    override fun getAccessRole() = ReaderRole.ADMIN

    override fun isWithContext() = true
}
