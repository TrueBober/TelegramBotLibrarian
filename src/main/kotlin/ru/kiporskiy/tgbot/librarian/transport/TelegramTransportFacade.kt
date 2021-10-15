package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Класс для взаимодействия с ботапи телеграма.
 */
class TelegramTransportFacade(private val bot: TelegramBot): MessengerTransport {

    /**
     * Слушатели для команд, поступающих для бота
     */
    private val commandsListener: MutableList<(MessengerTransport.MessengerCommand) -> Unit> = mutableListOf()
    /**
     * Слушатели для сообщений (кроме команд), поступающих от бота
     */
    private val messagesListener: MutableList<(MessengerTransport.MessengerTextMessage) -> Unit> = mutableListOf()

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
            message.text() != null && message.text().isBlank() -> {
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
    private fun getCommand(message: Message): MessengerTransport.MessengerCommand {
        val command = getMessageEntityText(message, message.entities().first())
        val chat = convertToChatId(message.chat())

        if (message.text().length == command.length) {
            return MessengerTransport.MessengerCommand(chat, command, listOf())
        } else {
            val arguments = message.text().substring(command.length).trim().split(" ")
            return MessengerTransport.MessengerCommand(chat, command, arguments)
        }
    }

    private fun getMessageEntityText(message: Message, entity: MessageEntity): String {
        val start = entity.offset()
        val end = entity.offset() + entity.length()
        return message.text().substring(start, end)
    }

    /**
     * Получить тексотвое сообщение
     */
    private fun getMessage(message: Message): MessengerTransport.MessengerTextMessage {
        val text = message.text()
        val chat = convertToChatId(message.chat())
        return MessengerTransport.MessengerTextMessage(chat, text)
    }

    /**
     * Отправка простого текстового сообщения
     */
    fun sendMessage(chatId: Long, text: String) {
        val request = SendMessage(chatId, text)
        bot.execute(request)
    }

    /**
     * Отправка простого текстового сообщения
     */
    override fun sendMessage(chatId: MessengerTransport.MessengerChatId, text: String) {
        val request = SendMessage(chatId.getId(), text)
        bot.execute(request)
    }

    /**
     * Отправка простого текстового сообщения
     */
    override fun sendMessage(message: TextMessage) {
        this.sendMessage(message.reader.id, message.text)
    }

    override fun addOnCommandListener(listener: (MessengerTransport.MessengerCommand) -> Unit) {
        this.commandsListener += listener
    }

    override fun addOnMessageListener(listener: (MessengerTransport.MessengerTextMessage) -> Unit) {
        this.messagesListener += listener
    }

    private fun convertToChatId(chat: Chat): MessengerTransport.MessengerChatId {
        return when(chat.type()) {
            Chat.Type.Private -> TelegramPrivateChat(chat.id(), chat.firstName(), chat.lastName(), chat.username())
            Chat.Type.group -> TelegramGroupChat(chat.id(), chat.title())
            Chat.Type.channel -> TelegramChannelChat(chat.id(), chat.title(), chat.username())
            Chat.Type.supergroup -> TelegramSupergroupChat(chat.id(), chat.title(), chat.username())
        }
    }
}

/**
 * Чат 1 на 1 с пользователем
 */
data class TelegramPrivateChat(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val username: String?
) : MessengerTransport.MessengerChatId {
    override fun getId() = id
}

/**
 * Групповой чат
 */
data class TelegramGroupChat(
    val id: Long,
    val title: String?
) : MessengerTransport.MessengerChatId {
    override fun getId() = id
}

/**
 * Чат с супергруппой
 */
data class TelegramSupergroupChat(
    val id: Long,
    val title: String?,
    val username: String?
) : MessengerTransport.MessengerChatId {
    override fun getId() = id
}

/**
 * Чат с каналом
 */
data class TelegramChannelChat(
    val id: Long,
    val title: String?,
    val username: String?
) : MessengerTransport.MessengerChatId {
    override fun getId() = id
}
