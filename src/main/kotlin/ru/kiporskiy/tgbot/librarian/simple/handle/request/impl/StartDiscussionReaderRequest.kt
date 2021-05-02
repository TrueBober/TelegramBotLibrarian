package ru.kiporskiy.tgbot.librarian.simple.handle.request.impl

import ru.kiporskiy.tgbot.librarian.simple.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.simple.handle.request.AbstractReaderRequest

object StartDiscussionReaderRequest : AbstractReaderRequest() {

    private const val command = "/start"

    private const val description = "начало обсуждения с ботом"

    override fun getCommand() = command

    override fun getDescription() = description

    //доступно всем пользователям
    override fun getAccessRole() = ReaderRole.USER
}
