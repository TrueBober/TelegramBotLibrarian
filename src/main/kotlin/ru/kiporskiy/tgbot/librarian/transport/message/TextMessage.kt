package ru.kiporskiy.tgbot.librarian.transport.message

import ru.kiporskiy.tgbot.librarian.transport.ChatId

/**
 * Обычное текстовое сообщение
 */
data class TextMessage(val text: String, val chatID: ChatId)
