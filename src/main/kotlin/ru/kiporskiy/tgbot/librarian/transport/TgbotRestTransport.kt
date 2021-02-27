package ru.kiporskiy.tgbot.librarian.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import ru.kiporskiy.tgbot.librarian.Reader
import ru.kiporskiy.tgbot.librarian.components.User
import ru.kiporskiy.tgbot.librarian.event.OnStartEvent
import ru.kiporskiy.tgbot.librarian.event.OnUnknownEvent
import ru.kiporskiy.tgbot.librarian.handler.EventHandler
import java.util.*

class TgbotRestTransport(botapiToken: String): TgbotTransport {

    private val bot: TelegramBot = TelegramBot(botapiToken)

    var handler: EventHandler? = null

    init {
        this.startListener()
    }

    override fun sendSimpleMessageText(message: String, reader: Reader) {
        val request = SendMessage(reader.user.id, message)
        this.bot.execute(request)
    }

    private fun startListener() {
        bot.setUpdatesListener {
            it.forEach { upd -> processUpdate(upd) }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    private fun processUpdate(update: Update) {
        update.message()?.let { this.processMessage(it) }
    }

    private fun processMessage(message:Message) {
        val from = message.from()
        val locale = Locale(from.languageCode())
        val user = User(from.id(), locale, from.username())
        when (message.text()) {
            "/start" -> handler?.handleEvent(OnStartEvent(user))
            else -> handler?.handleEvent(OnUnknownEvent(user))
        }
    }
}
