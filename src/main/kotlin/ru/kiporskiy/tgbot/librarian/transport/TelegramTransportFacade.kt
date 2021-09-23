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
class TelegramTransportFacade(private val bot: TelegramBot) {

    /**
     * Слушатели для команд, поступающих для бота
     */
    private val commandsListener: MutableList<(TelegramCommand) -> Unit> = mutableListOf()
    /**
     * Слушатели для сообщений (кроме команд), поступающих от бота
     */
    private val messagesListener: MutableList<(TelegramMessage) -> Unit> = mutableListOf()

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
    private fun getCommand(message: Message): TelegramCommand {
        val command = getMessageEntityText(message, message.entities().first())
        val chat = TelegramChat.of(message.chat())

        if (message.text().length == command.length) {
            return TelegramCommand(chat, command, listOf())
        } else {
            val arguments = message.text().substring(command.length).trim().split(" ")
            return TelegramCommand(chat, command, arguments)
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
    private fun getMessage(message: Message): TelegramMessage {
        val text = message.text()
        val chat = TelegramChat.of(message.chat())
        return TelegramMessage(chat, text)
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
    fun sendMessage(chat: TelegramChat, text: String) {
        sendMessage(chat.id, text)
    }

    /**
     * Добавить слушателя для команд, получаемых из телеграма
     */
    fun addOnCommandListener(listener: (TelegramCommand) -> Unit) {
        this.commandsListener += listener
    }

    /**
     * Добавить слушателя для команд, получаемых из телеграма
     */
    fun addOnMessageListener(listener: (TelegramMessage) -> Unit) {
        this.messagesListener += listener
    }
}

/**
 * Команда, полученная из телеграма
 */
data class TelegramCommand(val chat: TelegramChat, val command: String, val args: List<String>)

/**
 * Сообщение, полученное из телеграма
 */
data class TelegramMessage(val chat: TelegramChat, val message: String)

/**
 * Описание чата телеграма
 */
sealed interface TelegramChat {

    val id: Long

    companion object {
        fun of(chat: Chat): TelegramChat {
            return when(chat.type()) {
                Chat.Type.Private -> TelegramPrivateChat(chat.id(), chat.firstName(), chat.lastName(), chat.username())
                Chat.Type.group -> TelegramGroupChat(chat.id(), chat.title())
                Chat.Type.channel -> TelegramChannelChat(chat.id(), chat.title(), chat.username())
                Chat.Type.supergroup -> TelegramSupergroupChat(chat.id(), chat.title(), chat.username())
            }
        }
    }
}

/**
 * Чат 1 на 1 с пользователем
 */
data class TelegramPrivateChat(
    override val id: Long,
    val firstName: String?,
    val lastName: String?,
    val username: String?
) : TelegramChat

/**
 * Групповой чат
 */
data class TelegramGroupChat(
    override val id: Long,
    val title: String?
) : TelegramChat

/**
 * Чат с супергруппой
 */
data class TelegramSupergroupChat(
    override val id: Long,
    val title: String?,
    val username: String?
) : TelegramChat

/**
 * Чат с каналом
 */
data class TelegramChannelChat(
    override val id: Long,
    val title: String?,
    val username: String?
) : TelegramChat