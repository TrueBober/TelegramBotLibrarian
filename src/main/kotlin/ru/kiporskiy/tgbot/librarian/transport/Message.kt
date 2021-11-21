package ru.kiporskiy.tgbot.librarian.transport

/**
 * Обычное текстовое сообщение
 */
data class TextMessage(val text: String, val chatID: ChatId)

/**
 * Кнопка с надписью
 */
data class Button(val text: String)

/**
 * Простая клавиатура (двумерный массив кнопок)
 */
data class KeyboardMessage(val message: String, val buttons: List<List<Button>>, val chatID: ChatId)
