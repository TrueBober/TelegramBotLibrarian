package ru.kiporskiy.tgbot.librarian.transport

/**
 * Отправитель сообщений во "внешний мир" (например, в телеграм)
 */
interface Sender {

    /**
     * Отправить сообщение
     */
    fun sendMessage(message: OutboundMessage)

}
