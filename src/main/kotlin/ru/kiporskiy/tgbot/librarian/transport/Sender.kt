package ru.kiporskiy.tgbot.librarian.transport

import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Интерфейс отправителя сообщений пользователю
 */
interface Sender {

    /**
     * Отправить читателю текстовое сообщение
     */
    fun sendTextMessage(message: TextMessage)

}
