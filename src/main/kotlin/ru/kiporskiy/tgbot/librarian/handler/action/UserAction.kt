package ru.kiporskiy.tgbot.librarian.handler.action

import ru.kiporskiy.tgbot.librarian.components.Reader

/**
 * Действия, доступные пользователю
 */
interface UserAction {

    companion object {

        private val allActions = listOf(
            AddLibraryUserAction,
            AddLibraryAdminAction,
            GetLibrariesAction
        )

        fun getUserActions(reader: Reader): List<UserAction> {
            return when {
                reader.isSuperuser() -> allActions
                reader.isAdmin() -> allActions.filter { it.isForUser() || it.isForAdmin() }
                else -> allActions.filter { it.isForUser() }
            }
        }

    }


    /**
     * Действие, разрешенное обычному пользователю
     */
    fun isForUser(): Boolean = false

    /**
     * Действие, разрешенное админу
     */
    fun isForAdmin(): Boolean = false

    /**
     * Действие, разрешенное суперпользователю
     */
    fun isForSuperuser(): Boolean = false

    /**
     * Получить команду
     */
    fun getCommand(): String

    /**
     * Получить описание для команды
     */
    fun getCommandDescriptionKey(): String
}

/**
 * Создать новую библиотеку
 */
object AddLibraryUserAction : UserAction {

    override fun isForSuperuser() = true

    override fun getCommand() = "add_library"

    override fun getCommandDescriptionKey() = "action.add_library.description"

}

/**
 * Добавить в библиотеку администратора
 */
object AddLibraryAdminAction : UserAction {

    override fun isForSuperuser() = true

    override fun getCommand() = "add_admin"

    override fun getCommandDescriptionKey() = "action.add_admin.description"

}

/**
 * Получить список библиотек
 */
object GetLibrariesAction : UserAction {

    override fun isForSuperuser() = true

    override fun getCommand() = "get_libs"

    override fun getCommandDescriptionKey() = "action.get_libs.description"

}
