package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import ru.kiporskiy.tgbot.librarian.core.elements.User
import ru.kiporskiy.tgbot.librarian.event.OnCommandEvent
import ru.kiporskiy.tgbot.librarian.event.OnContextMessageEvent
import ru.kiporskiy.tgbot.librarian.handle.Handler
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Отправитель сообщений в телеграм
 */
object TelegramSender : Sender {

    private lateinit var bot: TelegramBot

    /**
     * Инициализация, используя ботапи
     */
    fun init(bot: TelegramBot) {
        this.bot = bot
        bot.setUpdatesListener(this::updatesListener)
    }

    /**
     * Инициализация, используя токен бота
     */
    fun init(token: String) {
        this.bot = TelegramBot(token)
        this.bot.setUpdatesListener(this::updatesListener)
    }

    /**
     * Слушатель всех событий, прилетающих для бота. Каждое событе обрабатывается, затем возвращается идентификатор
     * последнего прочитанного сообщения
     */
    internal fun updatesListener(updates: List<Update>): Int {
        updates.forEach { processUpdate(it) }
        return updates.last().updateId()
    }

    /**
     * Обработать полученное событие - обновление
     */
    private fun processUpdate(update: Update) {
        update.message()?.let { processUpdateMessage(it) }
    }

    /**
     * Обработать полученное сообщение
     */
    private fun processUpdateMessage(message: Message) {
        val user = telegramUserToApplicationUser(message.from())
        val firstMessageIsCommand = this.isTextStartsWithCommand(message)
        message.text()?.let { processTextMessage(user, it, firstMessageIsCommand) }
    }

    /**
     * Преобразовать пользователя Телеграма в пользователя системы
     */
    private fun telegramUserToApplicationUser(tgUser: com.pengrad.telegrambot.model.User): User {
        return User(
            tgUser.id().toInt(),
            tgUser.username() ?: "",
            tgUser.firstName() ?: "",
            tgUser.lastName() ?: ""
        )
    }

    /**
     * Сообщение пользователя начинается с команды
     */
    private fun isTextStartsWithCommand(message: Message): Boolean {
        val entities = message.entities() ?: return false
        val firstEntity = entities.firstOrNull() ?: return false
        return firstEntity.type() == MessageEntity.Type.bot_command
    }

    /**
     * Обработка текста сообщения.
     * Существуют 2 вида сообщений. Первые — команды (начинаются с символа '/'),
     * вторые — контекстно-зависимые сообщения
     */
    private fun processTextMessage(user: User, text: String, firstEntityIsCommand: Boolean) {
        if (firstEntityIsCommand) {
            Handler.handleEvent(OnCommandEvent(text, user))
        } else {
            Handler.handleEvent(OnContextMessageEvent(text, user))
        }
    }

    /**
     * Отправить текстовое сообщение от имени бота пользователю
     */
    override fun sendTextMessage(message: TextMessage) {
        val request = SendMessage(message.reader.user.id, message.text)
        this.bot.execute(request)
    }
}
