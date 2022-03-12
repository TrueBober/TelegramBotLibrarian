package ru.kiporskiy.tgbot.librarian.transport

/**
 * Интерфейс слушателя событий, получаемых извне
 */
interface EventListener {

    /**
     * Добавить слушателя для команд, получаемых извне
     */
    fun addOnCommandListener(listener: (MessengerCommand) -> Unit)

    /**
     * Добавить слушателя для текстовых сообщений, получаемых извне
     */
    fun addOnMessageListener(listener: (MessengerTextMessage) -> Unit)


    /**
     * Команда, получаемая от мессенджера (может иметь параметры)
     */
    data class MessengerCommand(val chatId: ChatId, val command: String, val args: List<String>)

    /**
     * Сообщение, получаемое от мессенджера
     */
    data class MessengerTextMessage(val chatId: ChatId, val message: String)
}


