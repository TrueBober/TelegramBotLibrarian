package ru.kiporskiy.tgbot.librarian.components

import java.lang.IllegalArgumentException

object Libraries {

    private val libs: MutableMap<String, Library> = HashMap()

    fun addLibrary(name: String) {
        if (libs.containsKey(name))
            throw IllegalArgumentException("Библиотека с таким названием уже есть")
        libs[name] = Library()
    }

    fun getLibsNames() = libs.keys.toList()

}
