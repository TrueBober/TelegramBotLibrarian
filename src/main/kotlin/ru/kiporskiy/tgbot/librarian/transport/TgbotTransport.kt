package ru.kiporskiy.tgbot.librarian.transport

import ru.kiporskiy.tgbot.librarian.User

interface TgbotTransport {

    /**
     * Отправить простой текст
     */
    fun sendSimpleMessageText(message: String, user: User)

}
