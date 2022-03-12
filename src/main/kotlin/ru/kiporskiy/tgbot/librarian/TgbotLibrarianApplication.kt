package ru.kiporskiy.tgbot.librarian

import com.pengrad.telegrambot.TelegramBot
import ru.kiporskiy.tgbot.librarian.handle.Handler
import ru.kiporskiy.tgbot.librarian.transport.TelegramTransportFacade
import java.lang.IllegalArgumentException

/**
 * Точка входа в приложение.
 * Принимает 1 параметр - токен бота, от имени которого будет осуществляться работа.
 */
fun main(args: Array<String>) {
    checkArgs(args)
    init(args[0])
}

/**
 * Проверка аргументов.
 * Убеждается, что в качестве аргументов было передано одно значение (токен бота).
 */
fun checkArgs(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("Аргументы: 'telegram_token'")
    }
}

/**
 * Инициализация транспорта, используя указанный токен.
 */
fun init(token: String) {
    val messengerTransport = TelegramTransportFacade(TelegramBot(token))
    Handler.initDefault(messengerTransport, messengerTransport)
}
