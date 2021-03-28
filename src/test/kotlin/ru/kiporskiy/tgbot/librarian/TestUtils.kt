package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.simple.elements.Book
import ru.kiporskiy.tgbot.librarian.simple.elements.User
import java.time.Year
import java.util.*
import kotlin.random.Random

fun getTestBook(isbn: String = "ISBN", category: Int? = null): Book {
    val title = "Title_" + Random.nextLong()
    val author = "Author_" + Random.nextLong()
    val year = Year.of(Random.nextInt(1970, 2020))
    val pages = Random.nextInt(100, 1000)
    val locale = Locale("ru")
    return Book(isbn, title, author, year, pages, category, locale)
}

fun getTestUser(
    id: Int = Random.nextInt(),
    username: String = "username ${Random.nextInt()}",
    firstname: String = "firstname ${Random.nextInt()}",
    lastname: String = "lastname ${Random.nextInt()}"
): User {
    return User(id, username, firstname, lastname)
}
