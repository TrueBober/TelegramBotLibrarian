package ru.kiporskiy.tgbot.librarian.handler

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import java.util.*
import kotlin.test.assertEquals

internal class DefaultLocationTest {

    lateinit var localization: DefaultLocation

    @BeforeEach
    internal fun setUp() {
        val path = this::class.java.classLoader.getResource("lang.json")!!.path
        localization = DefaultLocation(path)
    }

    @Test
    internal fun fileWasLoaded() {
        val result = localization.getMessage(Locale("ru-ru"), "test.message")
        assertEquals(result, "Простое тестовое сообщение")
    }

    @Test
    internal fun fileWasLoadedDefault() {
        val result = localization.getMessage(Locale("en-En"), "test.message2")
        assertEquals(result, "Простое тестовое сообщение по умолчанию")
    }

    @Test
    internal fun notFound() {
        assertThrows<IllegalStateException> {
            localization.getMessage(Locale("en-En"), "test.noMessage")
        }

    }
}
