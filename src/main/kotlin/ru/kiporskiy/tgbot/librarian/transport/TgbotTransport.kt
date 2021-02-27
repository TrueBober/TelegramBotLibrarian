package ru.kiporskiy.tgbot.librarian.transport

import ru.kiporskiy.tgbot.librarian.Reader

interface TgbotTransport {

    /**
     * Отправить простой текст
     */
    fun sendSimpleMessageText(message: String, reader: Reader)

}
