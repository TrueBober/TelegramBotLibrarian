package ru.kiporskiy.tgbot.librarian.transport

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Тестирование класса для взаимодействия с botapi телеграма.
 *
 * Note:
 * Классы библиотеки, которая используются для взимодействия с ботапи, не переопределяют equals, поэтому тестировать
 * вызовы методов с конкретными параметрами обычным сравнением (assertEquals(expected, actual)) не получится,
 * нужно будет залазить внутрь класса, чего делать не желательно, т.к. во-первых, от версии к версии классы будут
 * меняться (100% будут, т.к. ботапи развивается, как и библиотека, используемая в проекте), во-вторых, классы
 * библиотеки не имеют человеческих геттеров (см. com.pengrad.telegrambot.request.SendMessage, текст сообщения,
 * которое в него положили можно будет достать только через SendMessage.parameters["text"]).
 * Поэтому было принято решение тестировать только непосредственно вызов нужного метода библиотеки:
 * например, verify(bot).execute(any<SendMessage>()).
 *
 */
internal class TelegramTransportFacadeTest {

    private lateinit var bot: TelegramBot

    private lateinit var transport: TelegramTransportFacade

    @BeforeEach
    internal fun setUp() {
        bot = mock()
        transport = TelegramTransportFacade(bot)
    }

    @Test
    @DisplayName("Отправить простое текстовое сообщение")
    internal fun sendMessage_text() {
        val text = "Text"
        val chatId = UsernameTelegramChatId("id")
        transport.sendMessage(chatId, text)
        verify(bot).execute(any<SendMessage>())
    }
}
