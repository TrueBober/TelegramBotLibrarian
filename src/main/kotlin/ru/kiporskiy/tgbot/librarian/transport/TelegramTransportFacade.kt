package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

/**
 * Класс для взаимодействия с ботапи телеграма.
 */
class TelegramTransportFacade(private val bot: TelegramBot): EventListener, Sender {

    /**
     * Слушатели для команд, поступающих для бота
     */
    private val commandsListener: MutableList<(EventListener.MessengerCommand) -> Unit> = mutableListOf()
    /**
     * Слушатели для сообщений (кроме команд), поступающих от бота
     */
    private val messagesListener: MutableList<(EventListener.MessengerTextMessage) -> Unit> = mutableListOf()

    init {
        this.bot.setUpdatesListener { updatesListener(it) }
    }

    /**
     * Обработка полученной порции событий
     */
    private fun updatesListener(updates: List<Update>): Int {
        updates
            .mapNotNull { upd -> upd.message() }
            .forEach { processMessage(it) }
        return updates.last().updateId()
    }

    /**
     * Обработка сообщения
     */
    private fun processMessage(message: Message) {
        when {
            startWithCommand(message) -> {
                val command = getCommand(message)
                commandsListener.forEach { it.invoke(command) }
            }
            message.text() != null && message.text().isNotBlank() -> {
                val command = getMessage(message)
                messagesListener.forEach { it.invoke(command) }
            }
        }
    }

    /**
     * Проверить, что сообщение начинается с команды боту
     */
    private fun startWithCommand(message: Message): Boolean {
        if (message.text() == null || message.text().isBlank()) {
            return false
        }
        val entities = message.entities() ?: return false
        val firstEntity = entities.firstOrNull() ?: return false
        return firstEntity.type() == MessageEntity.Type.bot_command
    }

    /**
     * Получить команду с аргументами
     */
    private fun getCommand(message: Message): EventListener.MessengerCommand {
        val command = getMessageEntityText(message, message.entities().first())
        val chat = convertToChatId(message.chat())

        if (message.text().length == command.length) {
            return EventListener.MessengerCommand(chat, command, listOf())
        } else {
            val arguments = message.text().substring(command.length).trim().split(" ")
            return EventListener.MessengerCommand(chat, command, arguments)
        }
    }

    private fun getMessageEntityText(message: Message, entity: MessageEntity): String {
        val start = entity.offset()
        val end = entity.offset() + entity.length()
        return message.text().substring(start, end)
    }

    /**
     * Получить текстовое сообщение
     */
    private fun getMessage(message: Message): EventListener.MessengerTextMessage {
        val text = message.text()
        val chat = convertToChatId(message.chat())
        return EventListener.MessengerTextMessage(chat, text)
    }

    /**
     * Отправка простого текстового сообщения
     */
    override fun sendMessage(message: TextMessage) {
        val request = SendMessage(message.chatID.id, message.text)
        bot.execute(request)
    }

    override fun addOnCommandListener(listener: (EventListener.MessengerCommand) -> Unit) {
        this.commandsListener += listener
    }

    override fun addOnMessageListener(listener: (EventListener.MessengerTextMessage) -> Unit) {
        this.messagesListener += listener
    }

    private fun convertToChatId(chat: Chat): ChatId {
        return when(chat.type()) {
            Chat.Type.Private -> PrivateChat(chat.id(), chat.firstName(), chat.lastName(), chat.username())
            Chat.Type.group -> GroupChat(chat.id(), chat.title())
            Chat.Type.channel -> ChannelChat(chat.id(), chat.title(), chat.username())
            Chat.Type.supergroup -> SupergroupChat(chat.id(), chat.title(), chat.username())
        }
    }
}
