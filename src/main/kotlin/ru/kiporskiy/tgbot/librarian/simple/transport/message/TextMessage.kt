package ru.kiporskiy.tgbot.librarian.simple.transport.message

import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader

/**
 * Обычное текстовое сообщение
 */
data class TextMessage(val text: String, val reader: Reader)
