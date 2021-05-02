package ru.kiporskiy.tgbot.librarian.simple.transport

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import ru.kiporskiy.tgbot.librarian.simple.core.elements.User
import ru.kiporskiy.tgbot.librarian.simple.event.Event
import ru.kiporskiy.tgbot.librarian.simple.event.OnCommandEvent
import ru.kiporskiy.tgbot.librarian.simple.event.OnContextMessageEvent
import ru.kiporskiy.tgbot.librarian.simple.handle.Handler
import ru.kiporskiy.tgbot.librarian.simple.transport.message.TextMessage

object TelegramSender : Sender {

    lateinit var bot: TelegramBot

    fun init(token: String) {
        this.bot = TelegramBot(token)
        bot.setUpdatesListener(this::updatesListener)
    }

    private fun updatesListener(updates: List<Update>): Int {
        updates.forEach { processUpdate(it) }
        return updates.last().updateId()
    }

    private fun processUpdate(update: Update) {
        update.message()?.let { processUpdateMessage(it) }
    }

    private fun processUpdateMessage(message: Message) {
        val user = telegramUserToApplicationUser(message.from())
        message.text()?.let { processTextMessage(user, it) }
    }

    private fun telegramUserToApplicationUser(tgUser: com.pengrad.telegrambot.model.User): User {
        return User(tgUser.id(), tgUser.username(), tgUser.firstName(), tgUser.lastName())
    }

    private fun processTextMessage(user: User, text: String) {
        val event: Event =
            when {
                text.startsWith("/") -> OnCommandEvent(text, user)
                else -> OnContextMessageEvent(text, user)
            }
        Handler.handleEvent(event)
    }

    override fun sendTextMessage(message: TextMessage) {
        val request = SendMessage(message.reader.user.id, message.text)
        this.bot.execute(request)
    }
}
