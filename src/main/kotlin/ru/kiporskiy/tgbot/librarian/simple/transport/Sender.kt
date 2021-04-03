package ru.kiporskiy.tgbot.librarian.simple.transport

import ru.kiporskiy.tgbot.librarian.simple.transport.message.TextMessage

/**
 * Интерфейс отправителя сообщений пользователю
 */
interface Sender {

    /**
     * Отправить читателю текстовое сообщение
     */
    fun sendTextMessage(message: TextMessage)

}
