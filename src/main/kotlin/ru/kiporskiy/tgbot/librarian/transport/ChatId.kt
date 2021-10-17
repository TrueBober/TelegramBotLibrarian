package ru.kiporskiy.tgbot.librarian.transport

sealed interface ChatId {
    val id: Long
}

/**
 * Чат 1 на 1 с пользователем
 */
data class PrivateChat(
    override val id: Long,
    val firstName: String?,
    val lastName: String?,
    val username: String?
) : ChatId

/**
 * Групповой чат
 */
data class GroupChat(
    override val id: Long,
    val title: String?
) : ChatId

/**
 * Чат с супергруппой
 */
data class SupergroupChat(
    override val id: Long,
    val title: String?,
    val username: String?
) : ChatId

/**
 * Чат с каналом
 */
data class ChannelChat(
    override val id: Long,
    val title: String?,
    val username: String?
) : ChatId
