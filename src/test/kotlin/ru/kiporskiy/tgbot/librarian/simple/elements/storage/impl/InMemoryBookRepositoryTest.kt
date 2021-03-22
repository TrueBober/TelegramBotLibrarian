package ru.kiporskiy.tgbot.librarian.simple.elements.storage.impl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.kiporskiy.tgbot.librarian.simple.elements.Book
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

internal class InMemoryBookRepositoryTest {

    @BeforeEach
    internal fun setUp() {
        InMemoryBookRepository.reset()
        assertTrue(InMemoryBookRepository.findAll().isEmpty())
    }

    private fun getBook(isbn: String = "ISBN", category: Int? = null): Book {
        val title = "Title_" + Random.nextLong()
        val author = "Author_" + Random.nextLong()
        val year = Year.of(Random.nextInt(1970, 2020))
        val pages = Random.nextInt(100, 1000)
        val locale = Locale("ru")
        return Book(isbn, title, author, year, pages, category, locale)
    }

    @Test
    @DisplayName("Тест проверяет добавление и получение книги")
    internal fun addAndGet() {
        val book = getBook()
        InMemoryBookRepository.add(book)
        val books = InMemoryBookRepository.findAll()
        assertEquals(listOf(book), books)
    }

    @Test
    @DisplayName("Тест проверяет, что при добавлении нескольких книг с одинаковым ISBN добавляется только последняя")
    internal fun addWithOneIsbn() {
        val book1 = getBook("1")
        val book2 = getBook("2")
        val book1_2 = getBook("1")
        val book1_3 = getBook("1")

        InMemoryBookRepository.add(book1)
        InMemoryBookRepository.add(book2)
        InMemoryBookRepository.add(book1_2)
        InMemoryBookRepository.add(book1_3)

        val books = InMemoryBookRepository.findAll()
        assertEquals(2, books.size)
        assertTrue(books.containsAll(listOf(book1_3, book2)))
    }

    @Test
    @DisplayName("Поиск книг по категориям")
    internal fun findByCategory() {
        val book1 = getBook("1", 1)
        val book2 = getBook("2", 2)
        val book3 = getBook("3", 1)
        val book4 = getBook("4", 3)

        InMemoryBookRepository.add(book1)
        InMemoryBookRepository.add(book2)
        InMemoryBookRepository.add(book3)
        InMemoryBookRepository.add(book4)

        assertTrue(InMemoryBookRepository.find(1).containsAll(listOf(book1, book3)))
        assertTrue(InMemoryBookRepository.find(2).containsAll(listOf(book2)))
        assertTrue(InMemoryBookRepository.find(3).containsAll(listOf(book4)))
    }

    @Test
    @DisplayName("Постраничное получение книг с сортировкой по названию")
    internal fun findAllOrderByTitlePageable() {
        val size = 100
        val books = ArrayList<Book>()

        for (i in 0 until size) {
            val book = getBook(i.toString())
            books += book
            InMemoryBookRepository.add(book)
        }

        books.sortBy { b -> b.title }

        //первые 10 книг
        val firstBooks = books.subList(0, 10)
        val firstBooksTest = InMemoryBookRepository.findAllOrderByTitlePageable(0, 10)
        assertEquals(firstBooks, firstBooksTest)

        //вторые 10 книг
        val secondBooks = books.subList(10, 20)
        val secondBooksTest = InMemoryBookRepository.findAllOrderByTitlePageable(1, 10)
        assertEquals(secondBooks, secondBooksTest)

        //последние 10 книг
        val lastBooks = books.subList(90, 100)
        val lastBooksTest = InMemoryBookRepository.findAllOrderByTitlePageable(9, 10)
        assertEquals(lastBooks, lastBooksTest)

        //последние 5 книг
        val last5Books = books.subList(95, 100)
        val last5BooksTest = InMemoryBookRepository.findAllOrderByTitlePageable(1, 95)
        assertEquals(last5Books, last5BooksTest)
    }

    @Test
    @DisplayName("Постраничное получение книг определенной категории с сортировкой по названию")
    internal fun findWithCategoryOrderByTitlePageable() {
        val size = 100
        val booksA = ArrayList<Book>()
        val booksB = ArrayList<Book>()

        for (i in 0 until size) {
            val category = Random.nextInt(0, 2)
            val book = getBook(i.toString(), category)
            if (category == 0) booksA += book else booksB += book
            InMemoryBookRepository.add(book)
        }

        booksA.sortBy { b -> b.title }
        booksB.sortBy { b -> b.title }

        //первые 10 книг категории А
        val firstBooksA = booksA.subList(0, 10)
        val firstBooksATest = InMemoryBookRepository.findWithCategoryOrderByTitlePageable(0, 0, 10)
        assertEquals(firstBooksA, firstBooksATest)

        //вторые 10 книг категории Б
        val firstBooksB = booksB.subList(10, 20)
        val firstBooksBTest = InMemoryBookRepository.findWithCategoryOrderByTitlePageable(1, 1, 10)
        assertEquals(firstBooksB, firstBooksBTest)
    }
}
