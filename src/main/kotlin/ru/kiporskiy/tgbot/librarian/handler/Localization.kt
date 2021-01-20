package ru.kiporskiy.tgbot.librarian.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import ru.kiporskiy.tgbot.librarian.Reader
import java.io.FileReader
import java.util.*
import kotlin.collections.HashMap


interface Localization {

    fun getMessageForReader(reader: Reader, key: String, vararg params: String): String {
        return this.getMessage(reader.locale, key, *params)
    }

    fun getMessage(locale: Locale, key: String, vararg params: String): String

}


class DefaultLocation(pathToFile: String) : Localization {

    private val translations: Map<Locale, Translation>

    private val defaultLocation: Locale

    companion object {
        private val DEFAULT_LOCATION = Locale("ru-RU")
    }

    init {
        this.translations = loadStringsFromFile(pathToFile)
        this.defaultLocation = this.translations
            .map { it.value }
            .firstOrNull { it.defaultLocation }
            ?.lang ?: DEFAULT_LOCATION
    }

    private fun loadStringsFromFile(filepath: String): Map<Locale, Translation> {
        val reader = FileReader(filepath)
        val tree = ObjectMapper().readTree(reader)
        val result = HashMap<Locale, Translation>()
        tree.fields()
            .forEach {
                val locale = Locale(it.key)
                val translation = jsonNodeTreeToTranslation(locale, it.value)
                result[locale] = translation
            }
        return result
    }

    private fun jsonNodeTreeToTranslation(locale: Locale, node: JsonNode): Translation {
        val kv = HashMap<String, String>()
        var default = false
        node.fields().forEach {
            if (it.key == "default") {
                default = default or it.value.asBoolean()
            } else {
                kv[it.key] = it.value.asText()
            }
        }
        return Translation(default, locale, kv)
    }

    private class Translation(val defaultLocation: Boolean, val lang: Locale, val keyValue: Map<String, String>)

    override tailrec fun getMessage(locale: Locale, key: String, vararg params: String): String {
        val resultLocaleKeyValue = translations[locale]
        val resultLocaleString = resultLocaleKeyValue?.keyValue?.get(key)

        if (resultLocaleString != null)
            return resultLocaleString

        if (locale == DEFAULT_LOCATION)
            throw IllegalStateException("Строка не была найдена. Локаль: $locale, ключ: $key")
        else
            return this.getMessage(DEFAULT_LOCATION, key, *params)
    }
}
