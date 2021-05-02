package ru.kiporskiy.tgbot.librarian.core.elements

/**
 * Пользователь Telegram.
 */
data class User(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    /**
     * Проверяет, что все поля пользователей совпадают
     *
     * @return true, когда 2 объекта абсолютно аналогичны друг другу, false - иначе
     */
    fun isAnalog(user: User): Boolean {
        return user.id == id
                && user.firstName == firstName
                && user.lastName == lastName
                && user.username == username
    }
}
