package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.components.Book
import ru.kiporskiy.tgbot.librarian.components.BookCategory
import ru.kiporskiy.tgbot.librarian.components.BookCategory.Companion.getUnclassifiedCategory
import java.time.LocalDate
import java.time.LocalDateTime

private var COUNTER = 0

fun getCounterVal() = COUNTER++

fun getBook(
    title: String,
    author: String,
    createDate: LocalDateTime,
    numberSheets: Short,
    isbn: String,
    description: String = "",
    category: BookCategory = getUnclassifiedCategory(),
    publicationDate: LocalDate? = null
) = Book(title, author, createDate, numberSheets, isbn, description, category, publicationDate)

fun getBook(isbn: String, category: BookCategory): Book {
    val title = "title_" + getCounterVal()
    val author = "author_" + getCounterVal()
    val description = "description_" + getCounterVal()
    val createDate = LocalDateTime.now()!!
    val numberSheets = getCounterVal().toShort()
    return getBook(title, author, createDate, numberSheets, isbn, description, category)
}

fun getBook(isbn: String) = getBook(isbn, getUnclassifiedCategory())

fun getCategory(name: String) = BookCategory(name)

fun getCategory(name: String, parent: BookCategory) = BookCategory(name, parent)
