package ru.kiporskiy.tgbot.librarian.simple.elements

/**
 * Читатель - основная "единица", которой оперирует бизнес логика.
 */
data class Reader(val user: User, val role: ReaderRole)

/**
 * Роли читателей
 */
enum class ReaderRole {
    /**
     * Обычный читатель
     */
    USER,

    /**
     * Администратор
     */
    ADMIN,

    /**
     * Суперпользователь
     */
    SUPERUSER
}
