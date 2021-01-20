package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import ru.kiporskiy.tgbot.librarian.User

class TgbotRestTransport(botapiToken: String): TgbotTransport {

    private val bot: TelegramBot = TelegramBot(botapiToken)

    override fun sendSimpleMessageText(message: String, user: User) {
        val request = SendMessage(user.id.id, message)
        this.bot.execute(request)
    }
}
