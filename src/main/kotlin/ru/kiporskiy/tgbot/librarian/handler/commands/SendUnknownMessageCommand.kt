package ru.kiporskiy.tgbot.librarian.handler.commands

import ru.kiporskiy.tgbot.librarian.Reader
import ru.kiporskiy.tgbot.librarian.handler.Messages
import ru.kiporskiy.tgbot.librarian.transport.TgbotTransport

class SendUnknownMessageCommand(val transport: TgbotTransport, val reader: Reader) : TgbotLibrarianCommand {

    companion object {
        private const val messageKey = "notification.unknown"
    }

    override fun execute() {
        val message = Messages.getMessage(reader, messageKey, reader.user.name)
        transport.sendSimpleMessageText(message, reader.user)
    }


}