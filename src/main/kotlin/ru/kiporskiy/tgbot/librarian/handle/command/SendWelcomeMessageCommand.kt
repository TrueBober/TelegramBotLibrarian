package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.handle.request.impl.StartDiscussionReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Команда для отправки приветствия пользователю
 *
 * @param reader читатель, для которого выполняется команда
 * @param sender транспорт, по которому будет отправлено сообщение
 */
class SendWelcomeMessageCommand(private val sender: Sender, private val reader: Reader) : Command {

    companion object {
        const val message = "Приветствую. Я - бот-библиотекарь. С моей помощью Вы сможете забронировать книгу. Для " +
                "получения списка команд отправьте команду /get_command из любого меню"
    }

    override fun execute() {
        val textMessage = TextMessage(message, reader)
        sender.sendTextMessage(textMessage)
    }

    /**
     * Текущая команда предназначена для обработки на запрос создания новой беседы между пользователем и ботом
     */
    override fun getRequest() = StartDiscussionReaderRequest

}
