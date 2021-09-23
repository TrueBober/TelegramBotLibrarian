package ru.kiporskiy.tgbot.librarian.transport

import com.nhaarman.mockitokotlin2.*
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

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
        transport.sendMessage(123L, text)
        verify(bot).execute(any<SendMessage>())
    }

    @Test
    @DisplayName("Тестирование функции добавления слушателя на событие о получении команды для бота от телеграма")
    internal fun addOnCommandListener() {
        var success = false

        //параметры сообщения
        val title = "/command"
        val param1 = "argument1"
        val param2 = "argument2"
        val command = "$title $param1 $param2"
        val task: (TelegramCommand) -> Unit = {
            success = it.command == title && it.args == listOf(param1, param2)
        }

        //замокать обновление
        val update = this.mockMessageUpdate(command, 0, title.length, MessageEntity.Type.bot_command)

        var botapiUpdateListener: UpdatesListener? = null
        doAnswer {
            botapiUpdateListener = it.getArgument(0) as UpdatesListener
        }.whenever(bot).setUpdatesListener(any())

        //повторная инициализация, чтобы добавить слушателя, созданного выше
        transport = TelegramTransportFacade(bot)

        transport.addOnCommandListener(task)

        botapiUpdateListener?.process(listOf(update))

        assertTrue { success }
    }

    private fun mockMessageUpdate(text: String, entityStart: Int = 0, entityEnd: Int = 0, entityType: MessageEntity.Type?): Update {
        val chatId = 1000L
        val chat = mock<Chat>()
        doReturn(chatId).whenever(chat).id()
        doReturn(Chat.Type.channel).whenever(chat).type()

        val update = mock<Update>()
        val message = mock<Message>()
        doReturn(message).whenever(update).message()
        doReturn(text).whenever(message).text()
        doReturn(chat).whenever(message).chat()
        if (entityType != null) {
            val messageEntity = mock<MessageEntity>()
            doReturn(arrayOf(messageEntity)).whenever(message).entities()
            doReturn(entityStart).whenever(messageEntity).offset()
            doReturn(entityEnd).whenever(messageEntity).length()
            doReturn(entityType).whenever(messageEntity).type()
        }
        return update
    }
}
