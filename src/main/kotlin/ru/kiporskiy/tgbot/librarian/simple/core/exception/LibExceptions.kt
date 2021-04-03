package ru.kiporskiy.tgbot.librarian.simple.core.exception

import ru.kiporskiy.tgbot.librarian.simple.core.elements.BookCategory

/**
 * Родительский класс для всех исключений при добавлении новой категории
 */
abstract class AddCategoryException(val category: BookCategory): Exception()

/**
 * Исключение, выбрасываемое при попытке создать неуникальную категорию
 */
class BookCategoryAlreadyExistsException(category: BookCategory): AddCategoryException(category)

/**
 * Исключение, выбрасываемое при попытке создания категорий со слишком большой вложенностью
 */
class BookCategoryTooDeepNestingExistsException(category: BookCategory): AddCategoryException(category)

/**
 * Среди категорий найдена циклическая зависимость
 */
class BookCategoryLoopException(category: BookCategory): AddCategoryException(category)


abstract class UpdateCategoryException(val category: BookCategory): Exception()

/**
 * Обновляемая категория не найдена
 */
class CategoryNotFoundException(category: BookCategory): UpdateCategoryException(category)

/**
 * Исключение - книга уже забронирована
 */
object BookAlreadyBookingException: Exception()
