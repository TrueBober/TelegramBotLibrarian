package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import java.util.*

/**
 * Исходящее сообщение
 */
interface OutboundMessage {

    /**
     * Преобразование сообщения в запрос для BotAPI
     */
    fun toRequest(): BaseRequest<*, *>
}

/**
 * Обычное текстовое сообщение
 */
data class TextOutboundMessage(val text: String, val chatID: ChatId) : OutboundMessage {

    override fun toRequest() = SendMessage(chatID.id, text)

}

/**
 * Простая клавиатура (двумерный массив кнопок)
 */
data class KeyboardOutboundMessage(val message: String, val buttons: List<List<Button>>, val chatID: ChatId) :
    OutboundMessage {

    override fun toRequest(): SendMessage {
        val request = SendMessage(chatID.id, message)
        request.replyMarkup(this.getKeyboard())
        return request
    }

    private fun getKeyboard(): Keyboard {
        val rows = buttons.map { buttonsRow ->
            buttonsRow.map {
                KeyboardButton(it.text)
            }.toTypedArray()
        }.toTypedArray()

        val result = ReplyKeyboardMarkup(*rows)
        result.oneTimeKeyboard(true)
        return result
    }
}


/**
 * Кнопка с надписью
 */
data class Button(val text: String)
