package ru.kiporskiy.tgbot.librarian.handle

import ru.kiporskiy.tgbot.librarian.core.Context
import ru.kiporskiy.tgbot.librarian.core.EmptyContext
import ru.kiporskiy.tgbot.librarian.core.elements.ContextManager
import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.User
import ru.kiporskiy.tgbot.librarian.core.elements.storage.BookCategoryRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.BookRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.ReaderBookRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.ReaderRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.impl.InMemoryBookCategoryRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.impl.InMemoryBookRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.impl.InMemoryReaderBookingRepository
import ru.kiporskiy.tgbot.librarian.core.elements.storage.impl.InMemoryReaderRepository
import ru.kiporskiy.tgbot.librarian.handle.command.*
import ru.kiporskiy.tgbot.librarian.handle.request.ReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.*
import ru.kiporskiy.tgbot.librarian.transport.ChatId
import ru.kiporskiy.tgbot.librarian.transport.EventListener
import ru.kiporskiy.tgbot.librarian.transport.PrivateChat
import ru.kiporskiy.tgbot.librarian.transport.Sender

/**
 * Обработчик всех запросов
 */
object Handler {

    /**
     * Отправитель сообщений пользователю
     */
    private lateinit var sender: Sender

    /**
     * Репозиторий категорий
     */
    private lateinit var categoryRepo: BookCategoryRepository

    /**
     * Репозиторий книг
     */
    private lateinit var bookRepo: BookRepository

    /**
     * Репозиторий бронирования
     */
    private lateinit var readerBookRepo: ReaderBookRepository

    /**
     * Репозиторий читателей
     */
    private lateinit var readerRepo: ReaderRepository

    /**
     * Список всех возможных запросов
     */
    val accessibleRequests: List<ReaderRequest> =
        listOf(
            StartDiscussionReaderRequest,
            GetCommandsListReaderRequest,
            AddBookCategoryRequest,
            GetCategoriesListReaderRequest
        )


    /**
     * Инициализация с репозиториями по умолчанию (все хранится в памяти)
     */
    fun initDefault(sender: Sender, eventListener: EventListener) {
        this.sender = sender
        this.categoryRepo = InMemoryBookCategoryRepository
        this.bookRepo = InMemoryBookRepository
        this.readerBookRepo = InMemoryReaderBookingRepository
        this.readerRepo = InMemoryReaderRepository
        this.setEventListener(eventListener)
    }

    private fun setEventListener(eventListener: EventListener) {
        eventListener.addOnCommandListener {
            val reader = getEventUser(it.chatId) ?: return@addOnCommandListener
            this.handleCommand(reader, it.command)
        }

        eventListener.addOnMessageListener {
            val reader = getEventUser(it.chatId) ?: return@addOnMessageListener
            this.handleInputParam(reader, it.message)
        }
    }

    private fun getEventUser(chatId: ChatId): Reader? {
        return if (chatId is PrivateChat) {
            with(chatId) {
                val user = User(id.toInt(), username ?: "", firstName ?: "", lastName ?: "")
                return getReader(user)
            }
        } else {
            null
        }
    }

    /**
     * Преобразовать пользователя в читателя
     */
    private fun getReader(user: User) = this.readerRepo.getReader(user)

    /**
     * Обработать команду, полученную от пользователя
     */
    private fun handleCommand(reader: Reader, command: String) {
        //преобразовать команду пользователя в запрос
        val request = commandToRequest(command)

        if (request.isWithContext()) {
            //найти контекст запроса
            ContextManager.getContext(reader)
        } else {
            //если запрос не требует контекста - очистить контекст
            ContextManager.clearContext(reader)
        }

        //любая команда должна почистить контекст
        ContextManager.clearContext(reader)

        when (request) {
            is StartDiscussionReaderRequest -> SendWelcomeMessageCommand(sender, reader).execute()
            is GetCommandsListReaderRequest -> SendCommandsListCommand(sender, reader).execute()
            is AddBookCategoryRequest -> AddCategoryCommand(sender, reader, InMemoryBookCategoryRepository).execute()
            is GetCategoriesListReaderRequest -> GetCategoriesCommand(sender, reader, InMemoryBookCategoryRepository).execute()
            else -> SendUnknownRequestMessageCommand(sender, reader).execute()
        }
    }

    /**
     * Обработать команду, полученную от пользователя
     */
    private fun handleInputParam(reader: Reader, param: String) {
        val context: Context = ContextManager.getContext(reader)
        if (context !is EmptyContext) {
            context.setContextMessage(param)
            context.command?.execute(context)
        } else {
            SendUnknownRequestMessageCommand(sender, reader)
        }
    }

    /**
     * Преобразовать команду пользователя в запрос
     */
    private fun commandToRequest(userCommand: String): ReaderRequest {
        return this.accessibleRequests.firstOrNull { it.isCommand(userCommand) } ?: UnknownReaderRequest
    }
}
