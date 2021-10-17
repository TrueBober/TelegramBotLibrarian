package ru.kiporskiy.tgbot.librarian.transport

/**
 * Отправитель сообщений во "внешний мир" (например, в телеграм)
 */
interface Sender {

    /**
     * Отправка простого текстового сообщения
     *
     * @param message текстовое сообщение, которое необходимо отправить во "внешний мир"
     */
    fun sendMessage(message: TextMessage)

}
