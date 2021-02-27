package ru.kiporskiy.tgbot.librarian.event

import ru.kiporskiy.tgbot.librarian.components.User

/**
 * События, возникающие внутри сервиса
 */
interface LibrarianEvent

/**
 * Событие, возникающее, когда пользователь начал диалог с ботом
 */
data class OnStartEvent(val user: User): LibrarianEvent

/**
 * Неизвестное событие
 */
data class OnUnknownEvent(val user: User): LibrarianEvent
