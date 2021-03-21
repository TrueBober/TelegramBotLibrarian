package ru.kiporskiy.tgbot.librarian.simple.elements

import java.time.Year
import java.util.*

/**
 * Книга, зарегистрированная в системе
 */
data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val year: Year,
    val pages: Int,
    val category: Int? = null,
    val lang: Locale
)
