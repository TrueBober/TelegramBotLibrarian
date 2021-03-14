package ru.kiporskiy.tgbot.librarian.handler.commands

import ru.kiporskiy.tgbot.librarian.components.Reader
import ru.kiporskiy.tgbot.librarian.handler.Messages
import ru.kiporskiy.tgbot.librarian.transport.TgbotTransport

/**
 * Команда выполняет отправку сообщения пользователю о том, что сервер получил от него неизвестную команду
 */
class SendUnknownMessageCommand(private val transport: TgbotTransport, private val reader: Reader) : TgbotLibrarianCommand {

    companion object {
        private const val messageKey = "notification.unknown"
    }

    override fun execute() {
        val message = Messages.getMessage(reader, messageKey, reader.user.name)
        this.transport.sendSimpleMessageText(message, reader)
    }


}
