package ru.kiporskiy.tgbot.librarian.simple.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import ru.kiporskiy.tgbot.librarian.simple.transport.message.TextMessage

object TelegramSender : Sender {

    lateinit var bot: TelegramBot

    fun init(token: String) {
        this.bot = TelegramBot(token)
    }

    override fun sendTextMessage(message: TextMessage) {
        val request = SendMessage(message.reader.user.id, message.text)
        this.bot.execute(request)
    }
}
