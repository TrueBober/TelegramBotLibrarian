package ru.kiporskiy.tgbot.librarian.handler.commands

import ru.kiporskiy.tgbot.librarian.components.Reader
import ru.kiporskiy.tgbot.librarian.handler.Messages
import ru.kiporskiy.tgbot.librarian.handler.action.UserAction
import ru.kiporskiy.tgbot.librarian.transport.TgbotTransport
import java.lang.StringBuilder

/**
 * Команда, выполняемая при старте диалога пользователя с ботом.
 * Отправляет пригласительное сообщение и предлагает выбрать дальнейшие действия
 */
class SendActionsListCommand(
    private val transport: TgbotTransport,
    private val reader: Reader
) : TgbotLibrarianCommand {

    companion object {
        private const val messageKeyChooseCommand = "notification.select_command"
        private const val messageKeyNoCommands = "notification.no_command"
    }

    override fun execute() {
        val actions = UserAction.getUserActions(reader)
        val messageText =
            if (actions.isEmpty()) {
                Messages.getMessage(reader, messageKeyNoCommands, reader.user.name)
            } else {
                val chooseCommandMessage = Messages.getMessage(reader, messageKeyChooseCommand, reader.user.name)
                val msgBuilder = StringBuilder("$chooseCommandMessage\n\n")
                for (action in actions) {
                    val command = "/" + action.getCommand()
                    val descriptionKey = action.getCommandDescriptionKey()
                    val commandDescription = Messages.getMessage(reader, descriptionKey, reader.user.name)
                    msgBuilder.append(command).append(" - ").append(commandDescription)
                }
                msgBuilder.toString()
            }

        transport.sendSimpleMessageText(messageText, reader)
    }


}
