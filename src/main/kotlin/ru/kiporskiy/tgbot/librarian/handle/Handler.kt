package ru.kiporskiy.tgbot.librarian.handle

import ru.kiporskiy.tgbot.librarian.handle.command.Command
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
import ru.kiporskiy.tgbot.librarian.event.OnContextMessageEvent
import ru.kiporskiy.tgbot.librarian.handle.command.SendWelcomeMessageCommand
import ru.kiporskiy.tgbot.librarian.transport.Sender
import ru.kiporskiy.tgbot.librarian.transport.TelegramSender

/**
 * Обработчик всех запросов
 */
object Handler {
    private const val START_COMMAND_TEXT = "/start"

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
            is OnContextMessageEvent -> onStart(reader)
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
        when (command) {
            START_COMMAND_TEXT -> onStart(reader)
        }
    }

    /**
     * Событие о том, что пользователь инициировал "начало" общения с ботом преобразовать в команду
     */
    private fun onStart(reader: Reader): Command = SendWelcomeMessageCommand(sender, reader)
}
