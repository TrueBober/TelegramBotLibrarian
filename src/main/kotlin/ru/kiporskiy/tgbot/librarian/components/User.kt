package ru.kiporskiy.tgbot.librarian.components

import java.util.*

/**
 * Личные данные пользователя
 *
 * @param id уникальный идентификатор пользователя
 * @param locale язык, на котором будут отправлены сообщения пользователю
 * @param name имя пользователя
 *
 * прим. equals и hashcode перегружены, для соблюдения контракта "согласованности": т.е. если пользователь сменит,
 * например, имя, то другим пользователем он от этого не станет.
 */
data class User(val id: Int, val locale: Locale, val name: String) {

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
