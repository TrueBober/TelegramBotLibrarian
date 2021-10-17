package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.handle.Handler
import ru.kiporskiy.tgbot.librarian.handle.request.ReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.GetCommandsListReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.StartDiscussionReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Команда для отправки списка доступных пользователю команд
 *
 * @param reader читатель, для которого выполняется команда
 * @param sender транспорт, по которому будет отправлено сообщение
 */
class SendCommandsListCommand(private val sender: Sender, private val reader: Reader) : Command {

    companion object {
        const val message = "Список доступных команд: \n"
    }

    override fun execute() {
        val commands = getCommands()
        val stringCommands = toString(commands)
        val message = "$message\n$stringCommands"
        val textMessage = TextMessage(message, reader)
        sender.sendMessage(textMessage)
    }

    override fun getRequest() = GetCommandsListReaderRequest

    /**
     * Получить список команд, достпных пользователю
     */
    private fun getCommands() = Handler.accessibleRequests
        .filterNot { it is StartDiscussionReaderRequest }
        .filter { it.hasAccess(reader) }

    /**
     * Преобразовать список команд в строку
     */
    private fun toString(commands: List<ReaderRequest>): String {
        return commands.joinToString(separator = "\n", transform = { "${it.getCommand()} - ${it.getDescription()}" })
    }
}
