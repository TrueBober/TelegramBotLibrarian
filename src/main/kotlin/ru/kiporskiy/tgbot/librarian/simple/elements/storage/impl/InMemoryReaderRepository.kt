package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import ru.kiporskiy.tgbot.librarian.simple.elements.Reader
import ru.kiporskiy.tgbot.librarian.simple.elements.ReaderFactory
import ru.kiporskiy.tgbot.librarian.simple.elements.User
import ru.kiporskiy.tgbot.librarian.simple.elements.storage.ReaderRepository
import java.util.concurrent.ConcurrentHashMap

/**
 * Реализация хранилища читателей в оперативной памяти
 */
object InMemoryReaderRepository : ReaderRepository {

    /**
     * Список логинов администраторов
     */
    private val admins: MutableSet<String> = ConcurrentHashMap.newKeySet()

    /**
     * Список логинов суперпользователей
     */
    private val superusers: MutableSet<String> = ConcurrentHashMap.newKeySet()


    override fun getReader(user: User): Reader {
        return when {
            superusers.contains(user.username) -> ReaderFactory.getSuperuser(user)
            admins.contains(user.username) -> ReaderFactory.getAdmin(user)
            else -> ReaderFactory.getReader(user)
        }
    }

    override fun setAdmin(user: User) {
        superusers.remove(user.username)
        admins.add(user.username)
    }

    override fun setSuperuser(user: User) {
        admins.remove(user.username)
        superusers.add(user.username)
    }

    internal fun clear() {
        admins.clear()
        superusers.clear()
    }
}
