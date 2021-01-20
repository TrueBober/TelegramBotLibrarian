package ru.kiporskiy.tgbot.librarian.event

import ru.kiporskiy.tgbot.librarian.ReaderCardId

/**
 * События, возникающие внутри сервиса
 */
interface LibrarianEvent

/**
 * Событие, возникающее, когда пользователь начал диалог с ботом
 */
data class OnStartEvent(val id: ReaderCardId): LibrarianEvent
