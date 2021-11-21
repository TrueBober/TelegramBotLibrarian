package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.elements.BookCategory
import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.storage.BookCategoryRepository
import ru.kiporskiy.tgbot.librarian.handle.request.impl.GetCategoriesListReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.ChatId
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.TextMessage

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
        const val noCategoriesMessage = "Список категорий пуст"
    }

    override fun execute() {
        val categoriesListString = getCategoriesString()

        val textMessage = if (categoriesListString.isBlank())
            TextMessage(noCategoriesMessage, ChatId.getSimpleChatId(reader.id))
        else
            TextMessage(message + getCategoriesString(), ChatId.getSimpleChatId(reader.id))
        sender.sendMessage(textMessage)
    }

    override fun getRequest() = GetCategoriesListReaderRequest

    private fun getCategoriesString(): String {
        val builder = StringBuilder()
        val allCategories = repository.findAll()
        allCategories.forEach { addCategoriesString(builder, it) }
        return builder.toString()
    }

    private fun addCategoriesString(builder: StringBuilder, category: BookCategory) {
        for (i in 0 until category.getLevel())
            builder.append('-')
        builder.append(category.name).append('\n')
    }
}
