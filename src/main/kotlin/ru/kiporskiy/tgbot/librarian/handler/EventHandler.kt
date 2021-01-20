package ru.kiporskiy.tgbot.librarian.handler

import ru.kiporskiy.tgbot.librarian.event.LibrarianEvent

/**
 * Обработчик событий
 */
interface EventHandler {

    /**
     * Обработать событие
     */
    fun handleEvent(event: LibrarianEvent)

}
