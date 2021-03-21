package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.simple.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.simple.elements.User
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class InMemoryReaderRepositoryTest {

    @BeforeEach
    internal fun setUp() {
        InMemoryReaderRepository.clear()
    }

    private fun getUser(
        id: Int = Random.nextInt(),
        username: String = "username ${Random.nextInt()}",
        firstname: String = "firstname ${Random.nextInt()}",
        lastname: String = "lastname ${Random.nextInt()}"
    ): User {
        return User(id, username, firstname, lastname)
    }

    @Test
    @DisplayName("Тест проверяет получение обычных читателей на основе пользователя")
    internal fun getRegularReader() {
        val user = getUser()
        val reader = InMemoryReaderRepository.getReader(user)
        assertEquals(ReaderRole.USER, reader.role)
        assertSame(user, reader.user)
    }

    @Test
    @DisplayName("Тест проверяет получение админов на основе пользователя")
    internal fun getAdminReader() {
        val user = getUser()
        InMemoryReaderRepository.setSuperuser(user)     //это действие будет заменено следующей операцией
        InMemoryReaderRepository.setAdmin(user)
        val reader = InMemoryReaderRepository.getReader(user)
        assertEquals(ReaderRole.ADMIN, reader.role)
        assertSame(user, reader.user)
    }

    @Test
    @DisplayName("Тест проверяет получение суперпользователей на основе пользователя")
    internal fun getSuReader() {
        val user = getUser()
        InMemoryReaderRepository.setAdmin(user)         //это действие будет заменено следующей операцией
        InMemoryReaderRepository.setSuperuser(user)
        val reader = InMemoryReaderRepository.getReader(user)
        assertEquals(ReaderRole.SUPERUSER, reader.role)
        assertSame(user, reader.user)
    }
}
