package ru.kiporskiy.tgbot.librarian.simple.elements

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
}
