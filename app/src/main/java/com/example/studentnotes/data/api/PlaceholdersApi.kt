package com.example.studentnotes.data.api

import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.Request
import java.util.*

fun getEventsList() = listOf(
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = Date(2022, 4, 7, 14, 45),
        creationDate = Date(2022, 4, 3, 21, 30),
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = Date(2022, 4, 7, 14, 45),
        creationDate = Date(2022, 4, 3, 21, 30),
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = Date(2022, 4, 7, 14, 45),
        creationDate = Date(2022, 4, 3, 21, 30),
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю Новый тест по старому модулю модулю модулю модулю",
        author = "Алексей Воробьев",
        eventDate = Date(2022, 4, 7, 14, 45),
        creationDate = Date(2022, 4, 3, 21, 30),
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = Date(2022, 4, 7, 14, 45),
        creationDate = Date(2022, 4, 3, 21, 30),
        group = "М8О-203М-20"
    )
)

fun getGroupsList() = listOf(
    Group(
        title = "М8О-203М-20",
        description = "Самые крутые ребята",
        creator = "Алексей Воробьев",
        isPrivate = true
    ),
    Group(
        title = "М8О-103М-21",
        description = "Тоже ничего такие",
        creator = "Кое Кто",
        isPrivate = false
    )
)

fun getRequestsList() = listOf(
    Request(
        author = "Макар Павлов",
        group = "М8О-203М-20",
        message = "Я передумал, пустите меня обратно..."
    ),
    Request(
        author = "Антон Ларин",
        group = "Военная кафедра",
        message = "Плиз"
    ),
    Request(
        author = "Алексей Винников",
        group = "Инфопоиск"
    )
)