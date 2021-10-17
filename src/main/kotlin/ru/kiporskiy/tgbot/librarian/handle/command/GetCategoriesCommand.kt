package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.storage.BookCategoryRepository
import ru.kiporskiy.tgbot.librarian.handle.request.impl.GetCategoriesListReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.ChatId
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage

/**
 * Команда для получения списка всех категорий
 *
 * @param reader читатель, для которого выполняется команда
 * @param sender транспорт, по которому будет отправлено сообщение
 * @param repository репозиторий категорий
 */
class GetCategoriesCommand(
    private val sender: Sender,
    private val reader: Reader,
    private val repository: BookCategoryRepository
) : Command {

    companion object {
        const val message = "Список категорий книг...\n\n"
    }

    override fun execute() {
        val textMessage = TextMessage(message + getCategoriesString(), ChatId.getSimpleChatId(reader.id))
        sender.sendMessage(textMessage)
    }

    override fun getRequest() = GetCategoriesListReaderRequest

    private fun getCategoriesString(): String {
        val builder = StringBuilder()
        repository.findAll().forEach { addCategoriesString(builder, 0, it) }
        return builder.toString()
    }

    private fun addCategoriesString(builder: StringBuilder, level: Int, category: BookCategory) {
        for (i in 0 until level)
            builder.append('-')
        builder.append(' ').append(category.name).append('\n')
    }
}
