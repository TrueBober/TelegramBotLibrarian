package ru.kiporskiy.tgbot.librarian.handle.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.getTestReader
import ru.kiporskiy.tgbot.librarian.getTestSender
import ru.kiporskiy.tgbot.librarian.handle.request.impl.UnknownReaderRequest

internal class SendUnknownRequestMessageCommandTest {

    @Test
    @DisplayName("Проверка успешной отправки сообщения о неизвестной команде")
    internal fun execute() {
        val sender = getTestSender()
        val reader = getTestReader()
        val command = SendUnknownRequestMessageCommand(sender, reader)

        command.execute()

        assertEquals(1, sender.sentMessages.size)
        assertEquals(SendUnknownRequestMessageCommand.message, sender.sentMessages[0].text)
        assertSame(reader, sender.sentMessages[0].reader)
    }

    @Test
    @DisplayName("Проверить, что при получении параметров запроса отправляется именно UnknownReaderRequest")
    internal fun getRequest() {
        val sender = getTestSender()
        val reader = getTestReader()
        val command = SendUnknownRequestMessageCommand(sender, reader)
        assertEquals(UnknownReaderRequest, command.getRequest())
    }
}
