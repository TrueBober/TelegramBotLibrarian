package ru.kiporskiy.tgbot.librarian

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

interface Reader {

    /**
     * Язык, на котором происходит общение с пользователем
     */
    val locale: Locale

    /**
     * Пользователь
     */
    val user: User

    /**
     * Получить рекомендуемое время, через которое пользователь должен будет вернуть книгу
     */
    fun getRecommendedBookDuration(): Duration

}

interface Admin: Reader {
    /**
     * Получить права администратора
     */
    fun getAdminAccess(): Set<AdminAccess>
}

/**
 * Права администратора
 */
enum class AdminAccess {
    /**
     * Добавлять/удалять книги в библиотеку
     */
    ADMIN_BOOKS,

    /**
     * Добавлять/удалять читателей в библиотеку
     */
    ADMIN_USERS
}

interface Superuser: Admin {

    override fun getAdminAccess() = AdminAccess.values().toSet()
}


/**
 * "Обычный" читатель
 */
data class SimpleReader(override val user: User, override val locale: Locale): Reader {
    companion object {
        val DEFAULT_DURATION: Duration  = Duration.of(1, ChronoUnit.MONTHS)
    }
    override fun getRecommendedBookDuration() = DEFAULT_DURATION
}

/**
 * Администратор
 */
data class SimpleAdmin(override val user: User, val access: Set<AdminAccess>, override val locale: Locale): Admin {

    override fun getAdminAccess() = access

    override fun getRecommendedBookDuration() = SimpleReader.DEFAULT_DURATION
}

/**
 * "Обычный" читатель
 */
data class SimpleSuperuser(override val user: User, override val locale: Locale): Superuser {
    override fun getRecommendedBookDuration() = SimpleReader.DEFAULT_DURATION
}
