package ru.kiporskiy.tgbot.librarian.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import ru.kiporskiy.tgbot.librarian.Reader
import java.io.FileReader
import java.util.*

object Messages {

    private val translations: MutableMap<Locale, Translation> = HashMap()

    private val defaultLocation: Locale = Locale("ru")

    fun init(pathToFile: String) {
        if (translations.isNotEmpty()) {
            throw IllegalStateException()
        }
        translations.putAll(this.loadStringsFromFile(pathToFile))
    }

    private fun loadStringsFromFile(filepath: String): Map<Locale, Translation> {
        val reader = FileReader(filepath)
        val tree = ObjectMapper().readTree(reader)
        val result = HashMap<Locale, Translation>()
        tree.fields()
            .forEach {
                val locale = Locale(it.key)
                val translation = jsonNodeTreeToTranslation(it.value)
                result[locale] = translation
            }
        return result
    }

    private fun jsonNodeTreeToTranslation(node: JsonNode): Translation {
        val kv = HashMap<String, String>()
        node.fields().forEach {
            kv[it.key] = it.value.asText()
        }
        return Translation(kv)
    }

    /**
     * Получить сообщение для читателя по ключу
     */
    fun getMessage(reader: Reader, key: String, vararg params: String): String {
        val locale = reader.user.locale
        return this.getMessage(locale, key, *params)
    }

    /**
     * Получить сообщение для указанной локали по ключу
     */
    tailrec fun getMessage(locale: Locale, key: String, vararg params: String): String {
        val resultLocaleKeyValue = translations[locale]
        val resultLocaleString = resultLocaleKeyValue?.keyValue?.get(key)

        if (resultLocaleString != null)
            return resultLocaleString

        if (locale == defaultLocation)
            throw IllegalStateException("Строка не была найдена. Локаль: $locale, ключ: $key")
        else
            return this.getMessage(defaultLocation, key, *params)
    }
}

private class Translation(val keyValue: Map<String, String>)
