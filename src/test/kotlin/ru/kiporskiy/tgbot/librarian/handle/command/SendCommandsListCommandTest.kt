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

internal class SendCommandsListCommandTest {

    @Test
    @DisplayName("Проверка успешной отправки сообщения со списком всех комманд")
    internal fun execute() {
        val allCommands = Handler.accessibleRequests
            .filterNot { it is StartDiscussionReaderRequest }

        val sender = getTestSender()
        val reader = getTestReader(readerRole = ReaderRole.SUPERUSER)
        val command = SendCommandsListCommand(sender, reader)

        command.execute()

        assertEquals(1, sender.sentMessages.size)
        assertSame(reader, sender.sentMessages[0].reader)

        val messageText = sender.sentMessages[0].text
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
