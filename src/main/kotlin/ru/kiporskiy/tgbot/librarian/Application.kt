package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.components.Library
import ru.kiporskiy.tgbot.librarian.handler.DefaultEventHandler
import ru.kiporskiy.tgbot.librarian.handler.EventHandler
import ru.kiporskiy.tgbot.librarian.handler.Messages
import ru.kiporskiy.tgbot.librarian.transport.TgbotRestTransport

fun main(vararg args: String) {
    assert(args.size == 2)
    val botapiToken = args[0]
    args[1].split(',').forEach { SUPERUSERS_USERNAMES.add(it) }
    val transport = TgbotRestTransport(botapiToken)
    val library = Library()
    val eventHandler: EventHandler = DefaultEventHandler(transport, library)
    transport.handler = eventHandler

    Messages.init(Messages.javaClass.classLoader.getResource("lang.json").path)

    while (true){
        Thread.sleep(10)
    }
}
