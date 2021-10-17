package ru.kiporskiy.tgbot.librarian.transport

/**
 * Обычное текстовое сообщение
 */
data class TextMessage(val text: String, val chatID: ChatId)
