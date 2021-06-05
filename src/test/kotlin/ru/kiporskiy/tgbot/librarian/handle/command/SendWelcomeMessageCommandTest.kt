package ru.kiporskiy.tgbot.librarian.handle.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.getTestReader
import ru.kiporskiy.tgbot.librarian.getTestSender
import ru.kiporskiy.tgbot.librarian.handle.request.impl.StartDiscussionReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.UnknownReaderRequest

internal class SendWelcomeMessageCommandTest {

    @Test
    @DisplayName("Проверка успешной отправки пригласительного сообщения")
    internal fun execute() {
        val sender = getTestSender()
        val reader = getTestReader()
        val command = SendWelcomeMessageCommand(sender, reader)

        command.execute()

        assertEquals(1, sender.sentMessages.size)
        assertEquals(SendWelcomeMessageCommand.message, sender.sentMessages[0].text)
        assertSame(reader, sender.sentMessages[0].reader)
    }

    @Test
    @DisplayName("Проверить, что при получении параметров запроса отправляется именно StartDiscussionReaderRequest")
    internal fun getRequest() {
        val sender = getTestSender()
        val reader = getTestReader()
        val command = SendWelcomeMessageCommand(sender, reader)
        assertEquals(StartDiscussionReaderRequest, command.getRequest())
    }
}
