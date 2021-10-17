package ru.kiporskiy.tgbot.librarian.handle.command

import ru.kiporskiy.tgbot.librarian.core.BookCategoryContext
import ru.kiporskiy.tgbot.librarian.core.Context
import ru.kiporskiy.tgbot.librarian.core.elements.ContextManager
import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.storage.BookCategoryRepository
import ru.kiporskiy.tgbot.librarian.handle.request.impl.AddBookCategoryRequest
import ru.kiporskiy.tgbot.librarian.transport.ChatId
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.TextMessage

/**
 * Команда для отправки списка доступных пользователю команд
 *
 * @param reader читатель, для которого выполняется команда
 * @param sender транспорт, по которому будет отправлено сообщение
 * @param repository репозиторий категорий
 */
class AddCategoryCommand(
    private val sender: Sender,
    private val reader: Reader,
    private val repository: BookCategoryRepository
) : ContextCommand {

    companion object {
        const val message = "Введите название категории (или отправьте /get_commands для отмены)..."
        const val errorMessage = "Нет прав на добавление категорий"
        const val okMessage = "Категория успешно добавлена"
    }

    //пользователь ввел команду /add_category
    override fun execute() {
        if (!canAddCategory()) {
            val textMessage = TextMessage(errorMessage, ChatId.getSimpleChatId(reader.id))
            sender.sendMessage(textMessage)
        } else {
            val textMessage = TextMessage(message, ChatId.getSimpleChatId(reader.id))
            sender.sendMessage(textMessage)
            ContextManager.setContext(reader, BookCategoryContext("", this))
        }
    }

    //пользователь ввел название категории
    override fun execute(context: Context) {
        if (context is BookCategoryContext) {
            ContextManager.clearContext(reader)
            val category = context.categoryName
            checkCategoryName(category)
            this.repository.add(category, null)
            val textMessage = TextMessage(okMessage, ChatId.getSimpleChatId(reader.id))
            sender.sendMessage(textMessage)
            SendCommandsListCommand(sender, reader).execute()
        } else {
            execute()
        }
    }

    private fun canAddCategory() = AddBookCategoryRequest.hasAccess(reader)

    override fun getRequest() = AddBookCategoryRequest

    private fun checkCategoryName(category: String) {
        if (category.isBlank()) {
            throw IllegalArgumentException("Empty category name")
        }

        if (category.contains(0.toChar())) {
            throw IllegalArgumentException("Category has zero symbols")
        }

        if (category.length > 255) {
            throw IllegalArgumentException("Too long category name: $category")
        }
    }
}
