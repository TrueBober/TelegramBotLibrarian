package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.getTestUser
import ru.kiporskiy.tgbot.librarian.simple.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.simple.elements.User
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

internal class InMemoryReaderRepositoryTest {

    @BeforeEach
    internal fun setUp() {
        InMemoryReaderRepository.clear()
    }

    @Test
    @DisplayName("Тест проверяет получение обычных читателей на основе пользователя")
    internal fun getRegularReader() {
        val user = getTestUser()
        val reader = InMemoryReaderRepository.getReader(user)
        assertEquals(ReaderRole.USER, reader.role)
        assertSame(user, reader.user)
    }

    @Test
    @DisplayName("Тест проверяет получение админов на основе пользователя")
    internal fun getAdminReader() {
        val user = getTestUser()
        InMemoryReaderRepository.setSuperuser(user)     //это действие будет заменено следующей операцией
        InMemoryReaderRepository.setAdmin(user)
        val reader = InMemoryReaderRepository.getReader(user)
        assertEquals(ReaderRole.ADMIN, reader.role)
        assertSame(user, reader.user)
    }

    @Test
    @DisplayName("Тест проверяет получение суперпользователей на основе пользователя")
    internal fun getSuReader() {
        val user = getTestUser()
        InMemoryReaderRepository.setAdmin(user)         //это действие будет заменено следующей операцией
        InMemoryReaderRepository.setSuperuser(user)
        val reader = InMemoryReaderRepository.getReader(user)
        assertEquals(ReaderRole.SUPERUSER, reader.role)
        assertSame(user, reader.user)
    }

    @Test
    @DisplayName("Тест проверяет получение читателей по идентификатору")
    internal fun getReaderById() {
        val user1 = getTestUser(1)
        val user2 = getTestUser(2)
        val user3 = getTestUser(3)
        val reader1 = InMemoryReaderRepository.getReader(user1)
        val reader2 = InMemoryReaderRepository.getReader(user2)
        val reader3 = InMemoryReaderRepository.getReader(user3)

        val resultReader1 = InMemoryReaderRepository.getReader(1)
        val resultReader2 = InMemoryReaderRepository.getReader(2)
        val resultReader3 = InMemoryReaderRepository.getReader(3)
        val resultReaderUnknown = InMemoryReaderRepository.getReader(4)

        assertSame(reader1, resultReader1)
        assertSame(reader2, resultReader2)
        assertSame(reader3, resultReader3)
        assertNull(resultReaderUnknown)
    }
}
