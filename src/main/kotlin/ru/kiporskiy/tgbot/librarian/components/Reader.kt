package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.components.User
import java.time.Duration

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
data class SimpleReader(override val user: User) : Reader {
    companion object {
        val DEFAULT_DURATION: Duration = Duration.ofDays(30)
    }

    override fun getRecommendedBookDuration() = DEFAULT_DURATION
}

/**
 * Администратор
 */
data class SimpleAdmin(override val user: User, val access: Set<AdminAccess>) :
    Admin {

    override fun getAdminAccess() = access

    override fun getRecommendedBookDuration() = SimpleReader.DEFAULT_DURATION
}

/**
 * Суперпользователь
 */
data class SimpleSuperuser(override val user: User) : Superuser {
    override fun getRecommendedBookDuration() = SimpleReader.DEFAULT_DURATION
}

/**
 * Фабрика для классов читателей
 */
object ReaderFactory {

    fun getReader(user: User): Reader = SimpleReader(user)

    fun getAdmin(user: User, access: Set<AdminAccess>): Admin = SimpleAdmin(user, access)

    fun getSuperuser(user: User): Superuser = SimpleSuperuser(user)

}
