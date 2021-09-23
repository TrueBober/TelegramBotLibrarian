package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

/**
 * Класс для взаимодействия с ботапи телеграма.
 */
class TelegramTransportFacade(private val bot: TelegramBot) {

    /**
     * Слушатели для команд, поступающих от бота
     */
    private val commandsListener: MutableList<(TelegramCommand) -> Unit> = mutableListOf()

    init {
        this.runTelegramUpdatesListener()
    }

    /**
     * Запуск слушаетеля событий из телеграма
     */
    private fun runTelegramUpdatesListener() {
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
        val commandPosition = message.entities().first()
        val commandPositionStart = commandPosition.offset()
        val commandPositionEnd = commandPosition.offset() + commandPosition.length() + 1
        val command = message.text().substring(commandPositionStart, commandPositionEnd)

        if (message.text().length == command.length) {
            return TelegramCommand(command, listOf())
        } else {
            val arguments = message.text().substring(commandPositionEnd).split(" ")
            return TelegramCommand(command, arguments)
        }
    }

    /**
     * Отправка простого текстового сообщения
     */
    fun sendMessage(chatId: TelegramChatId, text: String) {
        val request = SendMessage(chatId.id, text)
        bot.execute(request)
    }

    /**
     * Добавить слушателя для команд, получаемых из телеграма
     */
    fun addOnTextMessageListener(listener: (TelegramCommand) -> Unit) {
        this.commandsListener += listener
    }
}

/**
 * Идентификатор чата телеграма
 */
sealed interface TelegramChatId {
    val id: Any

    companion object {
        /**
         * Создать объект интерфейса TelegramChatId на основе идентификатора чата, полученного от телеграма.
         * От телеграма может прилететь 2 типа идентификатора: логин пира (начинается с @), либо идентификатор пира (Int).
         */
        fun of(chatId: String): TelegramChatId {
            return if (chatId.startsWith("@"))
                UsernameTelegramChatId(chatId)
            else
                NumberTelegramChatId(chatId.toInt())
        }
    }
}

/**
 * Идентификатор, ассоциированный с логином пира
 */
data class UsernameTelegramChatId(override val id: String) : TelegramChatId

/**
 * Идентификатор, ассоциированный с числовым идентификатором пира
 */
data class NumberTelegramChatId(override val id: Int) : TelegramChatId

/**
 * Команда, полученная из телеграма
 */
data class TelegramCommand(val title: String, val args: List<String>)
