package ru.kiporskiy.tgbot.librarian.handle.command

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.getTestReader
import ru.kiporskiy.tgbot.librarian.getTestSender
import ru.kiporskiy.tgbot.librarian.handle.Handler
import ru.kiporskiy.tgbot.librarian.handle.request.impl.GetCommandsListReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.StartDiscussionReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.UnknownReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.ChatId
import ru.kiporskiy.tgbot.librarian.transport.TextOutboundMessage

internal class SendCommandsListCommandTest {

    @Test
    @DisplayName("Проверка успешной отправки сообщения со списком всех комманд")
    internal fun execute() {
        val allCommands = Handler.accessibleRequests
            .filterNot { it is StartDiscussionReaderRequest }

        val sender = getTestSender()
        val reader = getTestReader(readerRole = ReaderRole.SUPERUSER)
        val readerChatId = ChatId.getSimpleChatId(reader.id)
        val command = SendCommandsListCommand(sender, reader)

        command.execute()

        assertEquals(1, sender.sentMessages.size)
        assertEquals(readerChatId.id, (sender.sentMessages[0] as TextOutboundMessage).chatID.id)

        val messageText = (sender.sentMessages[0] as TextOutboundMessage).text
        assertTrue(messageText.startsWith(SendCommandsListCommand.message))
        allCommands.all { messageText.contains(it.getCommand()) && messageText.contains(it.getDescription()) }
    }

    @Test
    @DisplayName("Проверить, что при получении параметров запроса отправляется именно GetCommandsListReaderRequest")
    internal fun getRequest() {
        val sender = getTestSender()
        val reader = getTestReader()
        val command = SendCommandsListCommand(sender, reader)
        assertEquals(GetCommandsListReaderRequest, command.getRequest())
    }

}
