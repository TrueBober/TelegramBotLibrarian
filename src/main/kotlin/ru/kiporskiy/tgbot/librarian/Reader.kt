package ru.kiporskiy.tgbot.librarian

import java.time.Duration

interface Reader {
    /**
     * Получить рекомендуемое время, через которое пользователь должен будет вернуть книгу
     */
    fun getRecommendedBookDuration(): Duration
}

interface Admin: Reader

interface Superuser: Admin
