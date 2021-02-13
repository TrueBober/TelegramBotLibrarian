package ru.kiporskiy.tgbot.librarian.handler

import ru.kiporskiy.tgbot.librarian.*
import ru.kiporskiy.tgbot.librarian.event.LibrarianEvent
import ru.kiporskiy.tgbot.librarian.event.OnStartEvent
import ru.kiporskiy.tgbot.librarian.event.OnUnknownEvent
import ru.kiporskiy.tgbot.librarian.handler.commands.SendUnknownMessageCommand
import ru.kiporskiy.tgbot.librarian.handler.commands.SendWelcomeMessageCommand
import ru.kiporskiy.tgbot.librarian.transport.TgbotTransport
import kotlin.random.Random

/**
 * Обработчик событий
 */
interface EventHandler {

    /**
     * Обработать событие
     */
    fun handleEvent(event: LibrarianEvent)

}

class DefaultEventHandler(private val rgbotTransport: TgbotTransport,
                          private val library: Library): EventHandler {

    override fun handleEvent(event: LibrarianEvent) {
        when (event) {
            is OnStartEvent -> startDialog(event.user)
            is OnUnknownEvent -> sendUnknownEventMessage(event.user)
        }
    }

    private fun startDialog(user: User) {
        SendWelcomeMessageCommand(rgbotTransport, getReader(user)).execute()
    }

    private fun getReader(user: User): Reader {
        var reader = library.getReader(user)
        if (reader == null) {
            reader = SimpleReader(ReaderCardId(Random.nextLong()), user)
            library.addReader(reader)
        }
        return reader
    }

    private fun sendUnknownEventMessage(user: User) {
        SendUnknownMessageCommand(rgbotTransport, getReader(user)).execute()
    }

}
