package ru.kiporskiy.tgbot.librarian.simple.handle.request.impl

import ru.kiporskiy.tgbot.librarian.simple.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.simple.handle.request.AbstractReaderRequest

object GetCommandsListReaderRequest : AbstractReaderRequest() {

    private const val command = "/get_commands"

    private const val description = "получить список команд, доступных пользователю"

    override fun getCommand() = command

    override fun getDescription() = description

    //доступно всем пользователям
    override fun getAccessRole() = ReaderRole.USER
}
