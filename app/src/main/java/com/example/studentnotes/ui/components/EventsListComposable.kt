package com.example.studentnotes.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.User
import com.example.studentnotes.data.entities.stringifiedName

@Composable
fun EventsListComposable(
    modifier: Modifier = Modifier,
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    users: List<User>,
    groups: List<Group>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(events) { event ->
            EventCard(
                event = event,
                author = users.find { it.id == event.authorId }?.stringifiedName()
                    ?: "...",
                group = groups.find { it.id == event.groupId }?.title
                    ?: "...",
                onClick = { onEventClick(event) }
            )
        }
    }
}