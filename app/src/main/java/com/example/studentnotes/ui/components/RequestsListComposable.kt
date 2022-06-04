package com.example.studentnotes.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.studentnotes.data.entities.*

@Composable
fun RequestsListComposable(
    modifier: Modifier = Modifier,
    requests: List<Request>,
    currentUserId: String,
    onRequestClick: (Request) -> Unit,
    users: List<User>,
    groups: List<Group>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(requests) { request ->
            RequestCard(
                request = request,
                requestType = request.requestType(currentUserId),
                onRequestClick = { onRequestClick(request) },
                author = users.find { it.id == request.authorId }?.stringifiedName()
                    ?: "...",
                group = groups.find { it.id == request.groupId }?.title
                    ?: "..."
            )
        }
    }
}