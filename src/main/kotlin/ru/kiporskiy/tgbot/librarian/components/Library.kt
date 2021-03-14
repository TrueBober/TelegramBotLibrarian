package ru.kiporskiy.tgbot.librarian.components

import java.time.LocalDateTime
import java.util.concurrent.ConcurrentSkipListMap

/**
 * Книжная полка. Хранит набор книг
 */
class Bookshelf {

    /**
     * Отображение ISBN книги на саму книгу.
     * Хранит все доступные для бронирования книги
     */
    private val books: MutableMap<String, Book> = ConcurrentSkipListMap()

    /**
     * Получить книгу по ISBN
     */
    fun getBook(isbn: String) = books[isbn]

    /**
     * Получить список всех доступных книг
     */
    fun getBooks() = this.books.values.toList()

    /**
     * Получить список всех доступных книг для категории и всех ее потомков
     */
    fun getBooks(category: BookCategory): List<Book> {
        return books.values.filter { category.isParentFor(it.category) }
    }

    /**
     * Добавить новую книгу на полку.
     */
    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    operator fun plusAssign(element: Book) {
        addBook(element)
    }

    /**
     * Удалить книгу, если она есть
     */
    fun removeBook(book: Book) {
        books -= book.isbn
    }

    operator fun minusAssign(element: Book) {
        removeBook(element)
    }

    /**
     * Забронировать книгу.
     * Забронировать можно только свободную книгу.
     *
     * @throws IllegalStateException попытка забронировать книгу, которая не находится на полке
     */
    fun booking(book: Book) {
        books.remove(book.isbn) ?: throw IllegalStateException()
    }
}

/**
 * Библиотечный формуляр
 * Отображает какая книга у какого читателя "на руках".
 */
data class LibraryForm(
    val book: Book,
    val reader: Reader,
    val startDate: LocalDateTime,
    val recommendFinishDate: LocalDateTime
)

/**
 * Библиотека, объединяющая читателей и книги
 */
class Library {

    /**
     * Все читатели, зарегистрированные в системе
     */
    private val readers: MutableList<Reader> = ArrayList()

    /**
     * Все книги, зарегистрированные в системе
     */
    private val books: MutableList<Book> = ArrayList()

    /**
     * Все категории книг
     */
    private val categories: MutableList<BookCategory> = ArrayList()

    /**
     * Книги, доступные для чтения
     */
    private val bookshelf = Bookshelf()

    /**
     * Занятые книги
     */
    private val cards: List<LibraryForm> = ArrayList()

    /**
     * Добавить книгу в библиотеку
     */
    fun addBook(book: Book) {
        books += book
        bookshelf += book
    }

    /**
     * Удалить книгу из библиотеки
     */
    fun removeBook(book: Book) {
        books -= book
        bookshelf -= book
    }

    /**
     * Добавить новую категорию. "Родители" добавятся автоматически, если не были добавлены раньше
     */
    tailrec fun addCategoryBook(category: BookCategory) {
        if (category != BookCategory.getUnclassifiedCategory()) {
            categories += category
            addCategoryBook(category)
        }
    }

    /**
     * Удалить категорию книг вместе с дочерними категориями
     * Если у категории или ее потомков есть книги, то удалить ее будет нельзя
     *
     * @throws IllegalArgumentException при невозможности удалить категорию, у которой еще остались книги
     */
    fun removeCategoryBook(category: BookCategory) {
        val hasBooksWithCategory = books.any { category.isParentFor(it.category) }

        if (hasBooksWithCategory)
            throw IllegalArgumentException("Категорию нельзя удалить, потому что еще есть книги, которые в нее входят")
        else
            categories.removeIf { category.isParentFor(it) }
    }

    /**
     * Добавить книгу в библиотеку
     */
    fun addReader(reader: Reader) {
        readers += reader
    }

    /**
     * Получить читателя по номеру читательского билета
     */
    fun getReader(user: User) = readers.firstOrNull { r -> r.user == user }

    /**
     * Удалить книгу из библиотеки
     */
    fun removeReader(reader: Reader) {
        readers -= reader
    }

    /**
     * Получить все доступные книги для чтения
     */
    fun getAllBooksForReadings() = this.bookshelf.getBooks()

    /**
     * Постраничное получение всех доступных книг
     */
    fun getAllBooksForReadings(page: Int, pageSize: Int) = getAllBooksForReadingsPageable(page, pageSize)

    fun getAllBooksForReadingsPageable(page: Int, pageSize: Int, category: BookCategory? = null): List<Book> {
        val allBooks = category?.let { getAllBooksForReadings(it) } ?: getAllBooksForReadings()
        val startIndex = page * pageSize
        val finishIndex = startIndex + pageSize + 1

        if (allBooks.size <= startIndex)
            return listOf()

        return allBooks.subList(startIndex, finishIndex)
    }

    /**
     * Получить все доступные для чтения книги определенной категории
     */
    fun getAllBooksForReadings(category: BookCategory) = this.bookshelf.getBooks(category)

    /**
     * Получить все доступные для чтения книги определенной категории
     */
    fun getAllBooksForReadings(category: BookCategory, page: Int, pageSize: Int) =
        getAllBooksForReadingsPageable(page, pageSize, category)
}
