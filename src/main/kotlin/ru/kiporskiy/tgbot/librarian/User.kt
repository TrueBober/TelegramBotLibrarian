package ru.kiporskiy.tgbot.librarian

import java.util.*

/**
 * Личные данные пользователя
 *
 * @param id уникальный идентификатор пользователя
 * @param locale язык, на котором будут отправлены сообщения пользователю
 * @param name имя пользователя
 */
data class User(val id: Int, val locale: Locale, val name: String)
