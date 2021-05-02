package ru.kiporskiy.tgbot.librarian.transport.message

import ru.kiporskiy.tgbot.librarian.core.elements.Reader

/**
 * Обычное текстовое сообщение
 */
data class TextMessage(val text: String, val reader: Reader)
