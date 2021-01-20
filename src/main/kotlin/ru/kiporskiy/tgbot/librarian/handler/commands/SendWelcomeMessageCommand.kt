package ru.kiporskiy.tgbot.librarian.handler.commands

import ru.kiporskiy.tgbot.librarian.Reader
import ru.kiporskiy.tgbot.librarian.handler.Localization
import ru.kiporskiy.tgbot.librarian.transport.TgbotTransport

class SendWelcomeMessageCommand(val transport: TgbotTransport, val localization: Localization, val reader: Reader) :
    TgbotLibrarianCommand {

    companion object {
        private const val messageKey = "notification.welcome"
    }

    override fun execute() {
        val message = this.localization.getMessageForReader(reader, messageKey, reader.user.name)
        transport.sendSimpleMessageText(message, reader.user)
    }


}
