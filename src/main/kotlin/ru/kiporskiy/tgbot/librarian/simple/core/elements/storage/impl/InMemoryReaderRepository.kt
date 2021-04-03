package ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.impl

import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.simple.core.elements.ReaderFactory
import ru.kiporskiy.tgbot.librarian.simple.core.elements.User
import ru.kiporskiy.tgbot.librarian.simple.core.elements.storage.ReaderRepository
import java.util.concurrent.ConcurrentHashMap

/**
 * Реализация хранилища читателей в оперативной памяти
 */
object InMemoryReaderRepository : ReaderRepository {

    /**
     * Список зарегистрированных читателей
     */
    private val readers: MutableSet<Reader> = ConcurrentHashMap.newKeySet()

    /**
     * Список логинов администраторов
     */
    private val admins: MutableSet<String> = ConcurrentHashMap.newKeySet()

    /**
     * Список логинов суперпользователей
     */
    private val superusers: MutableSet<String> = ConcurrentHashMap.newKeySet()


    override fun getReader(user: User): Reader {
        val reader = readers.firstOrNull { it.user == user }
        if (reader != null && user.isAnalog(reader.user))
            return reader


        val result = when {
            superusers.contains(user.username) -> ReaderFactory.getSuperuser(user)
            admins.contains(user.username) -> ReaderFactory.getAdmin(user)
            else -> ReaderFactory.getReader(user)
        }
        readers.add(result)
        return result
    }

    override fun getReader(id: Long) = readers.firstOrNull { it.id == id }

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
        readers.clear()
    }
}
