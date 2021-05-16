package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.handle.request.impl.UnknownReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Команда для отправки сообщения о неизвестном запросе, полученном от пользователя
 *
 * @param reader читатель, для которого выполняется команда
 * @param sender транспорт, по которому будет отправлено сообщение
 */
class SendUnknownRequestMessageCommand(private val sender: Sender, private val reader: Reader) : Command {

    companion object {
        const val message = "Неизвестная команда. Для получения списка команд отправьте /get_command из любого меню"
    }

    override fun execute() {
        val textMessage = TextMessage(message, reader)
        sender.sendTextMessage(textMessage)
    }

    override fun getRequest() = UnknownReaderRequest
}
