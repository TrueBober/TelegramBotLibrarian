package ru.kiporskiy.tgbot.librarian.transport

import com.nhaarman.mockito_kotlin.*
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.User
import com.pengrad.telegrambot.request.SendMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.handle.Handler

internal class TelegramSenderTest {

    lateinit var bot: TelegramBot

    @BeforeEach
    internal fun setUp() {
        this.bot = mock()
        TelegramSender.init(this.bot)
        Handler.initDefault()
    }

    @Test
    internal fun inputCommand() {
        val update: Update = mock()
        val message: Message = mock()
        val from: User = mock()
        val entity: MessageEntity = mock()
        val text = "/start"

        doReturn(message).whenever(update).message()
        doReturn(from).whenever(message).from()
        doReturn(text).whenever(message).text()
        doReturn(arrayOf(entity)).whenever(message).entities()
        doReturn(MessageEntity.Type.bot_command).whenever(entity).type()

        TelegramSender.updatesListener(listOf(update))

        verify(this.bot).execute(any<SendMessage>())
    }

    @Test
    internal fun inputContextMessage() {
        val update: Update = mock()
        val message: Message = mock()
        val from: User = mock()
        val text = "Test"

        doReturn(message).whenever(update).message()
        doReturn(from).whenever(message).from()
        doReturn(text).whenever(message).text()

        TelegramSender.updatesListener(listOf(update))
    }
}
