package ru.kiporskiy.tgbot.librarian.handler.commands

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class KeyboardBuilderTest {

    @Test
    internal fun getKeyboard_exception_noButtons() {
        val builder = KeyboardBuilder()
        assertThrows<AssertionError> { builder.build() }
    }

    @Test
    internal fun getKeyboard_ok_oneButton() {
        val buttonText = "test"
        val builder = KeyboardBuilder()
        builder.add(buttonText)
        val result = builder.build()
        assertTrue { result is ReplyKeyboard }
        assertEquals(1, (result as ReplyKeyboard).buttons.size)
        assertEquals(1, result.buttons[0].size)
        assertEquals(buttonText, (result.buttons[0][0] as TextButton).text)
    }

    @Test
    internal fun getKeyboard_ok_twoLines() {
        val buttonText1 = "test1"
        val buttonText2 = "test2"
        val builder = KeyboardBuilder(1)
        builder.add(buttonText1)
        builder.add(buttonText2)
        val result = builder.build()
        assertTrue { result is ReplyKeyboard }
        assertEquals(2, (result as ReplyKeyboard).buttons.size)
        assertEquals(1, result.buttons[0].size)
        assertEquals(1, result.buttons[1].size)
        assertEquals(buttonText1, (result.buttons[0][0] as TextButton).text)
        assertEquals(buttonText2, (result.buttons[1][0] as TextButton).text)
    }
}
