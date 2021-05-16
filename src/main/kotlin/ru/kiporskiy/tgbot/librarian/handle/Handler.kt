package ru.kiporskiy.tgbot.librarian.handle

import ru.kiporskiy.tgbot.librarian.core.Context
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
import ru.kiporskiy.tgbot.librarian.event.Event
import ru.kiporskiy.tgbot.librarian.event.OnCommandEvent
import ru.kiporskiy.tgbot.librarian.handle.command.SendCommandsListCommand
import ru.kiporskiy.tgbot.librarian.handle.command.SendUnknownRequestMessageCommand
import ru.kiporskiy.tgbot.librarian.handle.command.SendWelcomeMessageCommand
import ru.kiporskiy.tgbot.librarian.handle.request.ReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.GetCommandsListReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.StartDiscussionReaderRequest
import ru.kiporskiy.tgbot.librarian.handle.request.impl.UnknownReaderRequest
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.TelegramSender

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
        listOf(StartDiscussionReaderRequest, GetCommandsListReaderRequest)


    /**
     * Инициализация с репозиториями по умолчанию (все хранится в памяти)
     */
    fun initDefault() {
        this.sender = TelegramSender
        this.categoryRepo = InMemoryBookCategoryRepository
        this.bookRepo = InMemoryBookRepository
        this.readerBookRepo = InMemoryReaderBookingRepository
        this.readerRepo = InMemoryReaderRepository
    }

    fun handleEvent(event: Event) {
        val reader = getReader(event.user)

        when (event) {
            is OnCommandEvent -> handleCommand(reader, event.command)
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
        //преобразовать коианду пользователя в запрос
        val request = commandToRequest(command)

        val context: Context? =
            if (request.isWithContext()) {
                //найти контекст запроса
                ContextManager.getContext(reader)
            } else {
                //если запрос не требует контекста - очистить контекст
                ContextManager.clearContext(reader)
                null
            }

        val cmd =
            when (request) {
                is StartDiscussionReaderRequest -> SendWelcomeMessageCommand(sender, reader)
                is GetCommandsListReaderRequest -> SendCommandsListCommand(sender, reader)
                else -> SendUnknownRequestMessageCommand(sender, reader)
            }

        cmd.execute()
    }

    /**
     * Преобразовать команду пользователя в запрос
     */
    private fun commandToRequest(userCommand: String): ReaderRequest {
        return this.accessibleRequests.firstOrNull { it.isCommand(userCommand) } ?: UnknownReaderRequest
    }
}
