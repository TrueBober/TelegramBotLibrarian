package ru.kiporskiy.tgbot.librarian

interface Reader

interface Admin: Reader

interface Superuser: Admin
