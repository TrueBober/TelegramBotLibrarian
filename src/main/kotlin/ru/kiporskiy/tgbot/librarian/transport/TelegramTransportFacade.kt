package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage

/**
 * Класс для взаимодействия с ботапи телеграма.
 */
class TelegramTransportFacade(private val bot: TelegramBot) {

    /**
     * Отправка простого текстового сообщения
     */
    fun sendMessage(chatId: TelegramChatId, text: String) {
        val request = SendMessage(chatId.id, text)
        bot.execute(request)
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
