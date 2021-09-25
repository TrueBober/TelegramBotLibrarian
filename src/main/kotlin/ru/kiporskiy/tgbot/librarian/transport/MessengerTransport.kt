package ru.kiporskiy.tgbot.librarian.transport

interface MessengerTransport {

    /**
     * Отправка простого текстового сообщения
     */
    fun sendMessage(chatId: MessengerChatId, text: String)

    /**
     * Добавить слушателя для команд, получаемых из мессенджера
     */
    fun addOnCommandListener(listener: (MessengerCommand) -> Unit)

    /**
     * Добавить слушателя для команд, получаемых из мессенджера
     */
    fun addOnMessageListener(listener: (MessengerTextMessage) -> Unit)


    /**
     * Интерфейс для идентификатора чата, куда отправлять сообщение
     */
    interface MessengerChatId {
        fun getId(): Any
    }

    /**
     * Команда, получаемая от мессенджера
     */
    data class MessengerCommand(val chatId: MessengerChatId, val command: String, val args: List<String>)

    /**
     * Сообщение, получаемое от мессенджера
     */
    data class MessengerTextMessage(val chatId: MessengerChatId, val message: String)
}

