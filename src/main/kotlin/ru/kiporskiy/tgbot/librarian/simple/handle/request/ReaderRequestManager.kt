package ru.kiporskiy.tgbot.librarian.simple.handle.request

import ru.kiporskiy.tgbot.librarian.simple.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.simple.handle.request.impl.GetCommandsListReaderRequest

/**
 * Менеджер доступных запросов
 */
object ReaderRequestManager {

    /**
     * Список всех доступных для выполнения запросов
     */
    private val requests: List<ReaderRequest> = listOf(
        GetCommandsListReaderRequest
    )

    /**
     * Получить список запросов, на которые у пользователя достаточно прав
     */
    fun getAccessableRequests(reader: Reader) = requests.filter { it.hasAccess(reader) }

}
