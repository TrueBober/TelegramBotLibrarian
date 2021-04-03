package ru.kiporskiy.tgbot.librarian.simple.event

import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader

/**
 * Классы событий
 */
sealed class Event

/**
 * Событие, когда пользователь начинает работу с ботом
 */
class OnStartEvent(val reader: Reader): Event()
