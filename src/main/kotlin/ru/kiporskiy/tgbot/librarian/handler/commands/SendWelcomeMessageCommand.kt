package ru.kiporskiy.tgbot.librarian.handler.commands

import ru.kiporskiy.tgbot.librarian.components.Reader
import ru.kiporskiy.tgbot.librarian.handler.Messages
import ru.kiporskiy.tgbot.librarian.transport.TgbotTransport

/**
 * Команда, выполняемая при старте диалога пользователя с ботом.
 * Отправляет пригласительное сообщение и предлагает выбрать дальнейшие действия
 */
class SendWelcomeMessageCommand(
    private val transport: TgbotTransport,
    private val reader: Reader
) : TgbotLibrarianCommand {

    companion object {
        private const val messageKey = "notification.welcome"
    }

    override fun execute() {
        val message = Messages.getMessage(reader, messageKey, reader.user.name)
        transport.sendSimpleMessageText(message, reader)

        SendActionsListCommand(transport, reader).execute()
    }


}
