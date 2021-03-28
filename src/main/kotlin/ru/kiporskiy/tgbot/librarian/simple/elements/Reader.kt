package ru.kiporskiy.tgbot.librarian.simple.elements

/**
 * Читатель - основная "единица", которой оперирует бизнес логика.
 * Для создания экземпляров необходимо использовать ReaderFactory
 */
data class Reader(val id: Long, val user: User, val role: ReaderRole)

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

/**
 * Фабрика для создания экземпляров "читателей"
 */
object ReaderFactory {

    /**
     * Получить обычного читателя
     */
    fun getReader(user: User) = Reader(user.id.toLong(), user, ReaderRole.USER)

    /**
     * Получить читателя с правами администратора
     */
    fun getAdmin(user: User) = Reader(user.id.toLong(), user, ReaderRole.ADMIN)

    /**
     * Получить читателя с правами суперпользователя
     */
    fun getSuperuser(user: User) = Reader(user.id.toLong(), user, ReaderRole.SUPERUSER)

}
