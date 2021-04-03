package ru.kiporskiy.tgbot.librarian.simple.handle

import ru.kiporskiy.tgbot.librarian.simple.handle.command.Command
import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.BookCategoryRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.BookRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.ReaderBookRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.ReaderRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.impl.InMemoryBookCategoryRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.impl.InMemoryBookRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.impl.InMemoryReaderBookingRepository
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.impl.InMemoryReaderRepository
import ru.kiporskiy.tgbot.librarian.simple.handle.command.SendWelcomeMessageCommand
import ru.kiporskiy.tgbot.librarian.simple.transport.Sender
import ru.kiporskiy.tgbot.librarian.simple.transport.TelegramSender

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
     * Инициализация с репозиториями по умолчанию (все хранится в памяти)
     */
    fun initDefault() {
        this.sender = TelegramSender
        this.categoryRepo = InMemoryBookCategoryRepository
        this.bookRepo = InMemoryBookRepository
        this.readerBookRepo = InMemoryReaderBookingRepository
        this.readerRepo = InMemoryReaderRepository
    }


    /**
     * Событие о том, что пользователь инициировал "начало" общения с ботом преобразовать в команду
     */
    fun onStart(reader: Reader): Command = SendWelcomeMessageCommand(sender, reader)
}
