package ru.kiporskiy.tgbot.librarian.handler.action

import ru.kiporskiy.tgbot.librarian.components.Reader

/**
 * Действия, доступные пользователю
 */
interface UserAction {

    companion object {

        private val allActions = listOf<UserAction>(AddLibraryUserAction)

        fun getUserActions(reader: Reader): List<UserAction> {
            return allActions.filter {
                it.isForUser()
                        || it.isForAdmin() && (reader.isAdmin() || reader.isSuperuser())
                        || it.isForSuperuser() && reader.isSuperuser()
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
