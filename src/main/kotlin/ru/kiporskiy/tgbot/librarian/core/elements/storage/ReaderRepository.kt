package ru.kiporskiy.tgbot.librarian.core.elements.storage

import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.User

/**
 * Репозиторий для читателей
 */
interface ReaderRepository {

    /**
     * Получить читателя из пользователя
     */
    fun getReader(user: User): Reader

    /**
     * Сделать пользователя администратором
     */
    fun setAdmin(user: User)

    /**
     * Сделать пользователя суперпользователем
     */
    fun setSuperuser(user: User)

    /**
     * Получить читателя по идентификатору
     */
    fun getReader(id: Long): Reader?

}
