package ru.kiporskiy.tgbot.librarian

import ru.kiporskiy.tgbot.librarian.core.elements.Book
import ru.kiporskiy.tgbot.librarian.core.elements.Reader
import ru.kiporskiy.tgbot.librarian.core.elements.ReaderRole
import ru.kiporskiy.tgbot.librarian.core.elements.User
import ru.kiporskiy.tgbot.librarian.transport.MessengerTransport
import ru.kiporskiy.tgbot.librarian.transport.message.TextMessage
import java.time.Year
import java.util.*
import kotlin.random.Random

fun getTestBook(isbn: String = "ISBN", category: Int? = null): Book {
    val title = "Title_" + Random.nextLong()
    val author = "Author_" + Random.nextLong()
    val year = Year.of(Random.nextInt(1970, 2020))
    val pages = Random.nextInt(100, 1000)
    val locale = Locale("ru")
    return Book(isbn, title, author, year, pages, category, locale)
}

fun getTestUser(
    id: Int = Random.nextInt(),
    username: String = "username ${Random.nextInt()}",
    firstname: String = "firstname ${Random.nextInt()}",
    lastname: String = "lastname ${Random.nextInt()}"
): User {
    return User(id, username, firstname, lastname)
}

fun getTestReader(
    id: Long = Random.nextLong(),
    readerRole: ReaderRole = ReaderRole.USER,
    userId: Int = Random.nextInt(),
    username: String = "username ${Random.nextInt()}",
    firstname: String = "firstname ${Random.nextInt()}",
    lastname: String = "lastname ${Random.nextInt()}"
): Reader {
    val user = getTestUser(userId, username, firstname, lastname)
    return Reader(id, user, readerRole)
}

/**
 * Тестовый "отправитель" сообщений.
 * Складывает все отправленные сообщения в список.
 */
class TestSender: MessengerTransport {

    val sentMessages: MutableList<TextMessage> = LinkedList()

    val sentMessagesPair: MutableList<Pair<MessengerTransport.MessengerChatId, String>> = LinkedList()

    val commandsListener: MutableList<(MessengerTransport.MessengerCommand) -> Unit> = LinkedList()

    val messageTextListener: MutableList<(MessengerTransport.MessengerTextMessage) -> Unit> = LinkedList()

    override fun sendMessage(chatId: MessengerTransport.MessengerChatId, text: String) {
        sentMessagesPair += chatId to text
    }

    override fun sendMessage(message: TextMessage) {
        this.sentMessages += message
    }

    override fun addOnCommandListener(listener: (MessengerTransport.MessengerCommand) -> Unit) {
        this.commandsListener += listener
    }

    override fun addOnMessageListener(listener: (MessengerTransport.MessengerTextMessage) -> Unit) {
        this.messageTextListener += listener
    }

}

/**
 * Получить тестового отправителя сообщений
 */
fun getTestSender() = TestSender()
