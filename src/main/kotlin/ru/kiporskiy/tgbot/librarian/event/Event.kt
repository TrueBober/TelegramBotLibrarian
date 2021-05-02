package ru.kiporskiy.tgbot.librarian.event

import ru.kiporskiy.tgbot.librarian.core.elements.User

/**
 * Классы событий
 */
sealed class Event(val user: User)

/**
 * Событие - команда, полученная от пользователя
 */
class OnCommandEvent(val command: String, user: User): Event(user)

/**
 * Событие - сообщение, зависимое от контекста
 */
class OnContextMessageEvent(val message: String, user: User): Event(user)
