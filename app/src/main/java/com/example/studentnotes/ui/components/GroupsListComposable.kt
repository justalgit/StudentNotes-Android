package com.example.studentnotes.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.User
import com.example.studentnotes.data.entities.stringifiedName

@Composable
fun GroupsListComposable(
    modifier: Modifier = Modifier,
    groups: List<Group>,
    users: List<User>,
    onGroupClick: (Group) -> Unit = {}
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(groups) { group ->
            GroupCard(
                group = group,
                author = users.find { it.id == group.creatorId }?.stringifiedName() ?: "...",
                onClick = { onGroupClick(group) }
            )
        }
    }
}