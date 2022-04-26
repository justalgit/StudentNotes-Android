package com.example.studentnotes.data.api

import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.Request

fun getEventsList() = listOf(
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = 12345L,
        lastModifiedDate = 12345L,
        lastModifiedUser = "Алексей Воробьев",
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = 12345L,
        lastModifiedDate = 12345L,
        lastModifiedUser = "Влад Петрушин",
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = 12345L,
        lastModifiedDate = 12345L,
        lastModifiedUser = "Анна Никитина",
        group = "М8О-203М-20",
        isEditable = false
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю Новый тест по старому модулю модулю модулю модулю",
        author = "Алексей Воробьев",
        eventDate = 12345L,
        lastModifiedDate = 12345L,
        lastModifiedUser = "Алексей Винников",
        group = "М8О-203М-20"
    ),
    Event(
        title = "Сделать ДЗ по английскому",
        description = "Новый тест по старому модулю",
        author = "Алексей Воробьев",
        eventDate = 12345L,
        lastModifiedDate = 12345L,
        lastModifiedUser = "Антон Ларин",
        group = "М8О-203М-20цшцофвдфцдвоыоафцлащфлыафцжылаужфжцааыдлацфдыжафуаывыау",
        isEditable = false
    )
)

fun getGroupsList() = listOf(
    Group(
        title = "М8О-203М-20цшцофвдфцдвоыоафцлащфлыафцжылаужфжцааыдлацфдыжафуаывыау",
        description = "Самые крутые ребята блин воще просто пушка вау это очень долгое описание",
        creator = "Алексей Воробьев",
        lastModifiedDate = 12345L,
        lastModifiedUser = "Антон Ларин",
        isPrivate = true,
        isEditable = true
    ),
    Group(
        title = "М8О-103М-21",
        description = "Тоже ничего такие",
        creator = "Кое Кто",
        lastModifiedDate = 12345L,
        lastModifiedUser = "Анонимус",
        isPrivate = false,
        isEditable = true
    ),
    Group(
        title = "М8О-203М-20",
        description = "Самые крутые ребята блин воще просто пушка вау это очень долгое описание",
        creator = "Алексей Воробьев",
        lastModifiedDate = 12345L,
        lastModifiedUser = "Антон Ларин",
        isPrivate = true,
        isEditable = false
    ),
    Group(
        title = "М8О-103М-21",
        description = "Тоже ничего такие",
        creator = "Кое Кто",
        lastModifiedDate = 12345L,
        lastModifiedUser = "Анонимус",
        isPrivate = false,
        isEditable = false
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