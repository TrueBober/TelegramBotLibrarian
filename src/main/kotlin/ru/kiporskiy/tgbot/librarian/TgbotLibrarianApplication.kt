package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.handle.Handler
import ru.kiporskiy.tgbot.librarian.transport.TelegramSender
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    checkArgs(args)
    init(args[0])
}

fun checkArgs(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("Аргументы: 'telegram_token'")
    }
}

fun init(token: String) {
    TelegramSender.init(token)
    Handler.initDefault()
}
