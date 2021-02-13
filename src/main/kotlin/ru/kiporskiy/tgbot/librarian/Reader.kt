package ru.kiporskiy.tgbot.librarian

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Читатель в библиотеке
 */
interface Reader {

    /**
     * Личные данные читателя
     */
    val user: User

    /**
     * Получить рекомендуемое время, через которое пользователь должен будет вернуть книгу
     */
    fun getRecommendedBookDuration(): Duration

    /**
     * Польчить язык, на котором отправлять сообщения пользователю
     */
    fun getReaderLocale() = user.locale

    /**
     * Ид читательского билета
     */
    val readerCardId: ReaderCardId

}

/**
 * Администратор системы
 */
interface Admin : Reader {
    /**
     * Получить права администратора
     */
    fun getAdminAccess(): Set<AdminAccess>
}

/**
 * Суперюзер
 */
interface Superuser : Admin {

    override fun getAdminAccess() = AdminAccess.values().toSet()
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

/**
 * "Обычный" читатель
 */
data class SimpleReader(override val readerCardId: ReaderCardId, override val user: User) : Reader {
    companion object {
        val DEFAULT_DURATION: Duration = Duration.ofDays(30)
    }

    override fun getRecommendedBookDuration() = DEFAULT_DURATION
}

/**
 * Администратор
 */
data class SimpleAdmin(override val readerCardId: ReaderCardId, override val user: User, val access: Set<AdminAccess>) :
    Admin {

    override fun getAdminAccess() = access

    override fun getRecommendedBookDuration() = SimpleReader.DEFAULT_DURATION
}

/**
 * "Обычный" читатель
 */
data class SimpleSuperuser(override val readerCardId: ReaderCardId, override val user: User) : Superuser {
    override fun getRecommendedBookDuration() = SimpleReader.DEFAULT_DURATION
}
