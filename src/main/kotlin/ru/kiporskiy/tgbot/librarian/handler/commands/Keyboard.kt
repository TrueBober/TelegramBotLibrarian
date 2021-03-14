package ru.kiporskiy.tgbot.librarian.handler.commands

import java.util.*

/**
 * Кнопки для отображения клавиатуры пользователю
 */
interface Button

/**
 * Кнопка, содержащая текст
 *
 * @param text текст на кнопке
 */
data class TextButton(val text: String) : Button


/**
 * Клавиатура, отображаемая пользователю для удобного использования бота
 */
interface Keyboard

/**
 * Классическая клавиатура, по нажатию на которую на сервер уходит текст кнопки, на которую нажал пользователь
 */
data class ReplyKeyboard(val buttons: List<List<Button>>) : Keyboard

/**
 * Создать новую клавиатуру
 *
 * @param buttonsInRow количество "кнопок" в одну строку (от 1 до 10. По-умолчанию, 3)
 * @param buttonText тексты на кнопках (минимальное количество текстовых строк - 1)
 */
fun getKeyboard(buttonsInRow: Int = 3, vararg buttonText: String): Keyboard {
    assert(buttonText.isNotEmpty()) { "Нужна хотя бы одна кнопка" }
    assert(buttonsInRow in 1..10) { "Некорректное количество кнопок в строке: $buttonsInRow" }

    val buttons = LinkedList<List<Button>>()
    var currentRow = LinkedList<Button>()
    buttons += currentRow

    for (i in buttonText.indices) {
        if (i > 0 && i % buttonsInRow == 0) {
            currentRow = LinkedList<Button>()
            buttons += currentRow
        }
        currentRow.add(TextButton(buttonText[i]))
    }

    return ReplyKeyboard(buttons)
}

/**
 * Класс для генерации клавиатуры
 *
 * @param buttonsInRow количество кнопок в одной строке
 */
class KeyboardBuilder(private val buttonsInRow: Int = 3) {

    private val buttons: LinkedList<String> = LinkedList()

    /**
     * Добавить кнопку с текстом для клавиатуры
     */
    fun add(button: String) {
        buttons += button
    }

    /**
     * Создать клавиатуру
     */
    fun build() = getKeyboard(buttonsInRow, *buttons.toTypedArray())
}
