package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.handler.DefaultEventHandler
import ru.kiporskiy.tgbot.librarian.handler.EventHandler
import ru.kiporskiy.tgbot.librarian.handler.Messages
import ru.kiporskiy.tgbot.librarian.transport.TgbotRestTransport

fun main() {
    val botapiToken = "268901840:AAGEMOzNCyTg1VSH32bJwSkiZEuZFRgvTcQ"
    val transport = TgbotRestTransport(botapiToken)
    val library = Library()
    val eventHandler: EventHandler = DefaultEventHandler(transport, library)
    transport.handler = eventHandler

    Messages.init(Messages.javaClass.classLoader.getResource("lang.json").path)

    while (true){
    }
}
