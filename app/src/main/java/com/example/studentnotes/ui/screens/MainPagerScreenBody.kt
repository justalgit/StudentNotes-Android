package com.example.studentnotes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.ui.components.UiTabItem

@Composable
fun MainPagerScreenBody(
    navController: NavController
) {
    val menus = listOf(
        TabData(stringResource(R.string.events), painterResource(R.drawable.ic_events_24)),
        TabData(stringResource(R.string.groups), painterResource(R.drawable.ic_groups_24)),
        TabData(stringResource(R.string.requests), painterResource(R.drawable.ic_requests_24)),
        TabData(stringResource(R.string.settings), painterResource(R.drawable.ic_settings_24))
    )
    var selectedMenu by remember {
        mutableStateOf(menus[0])
    }

    Scaffold {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                when(selectedMenu) {
                    menus[0] -> EventsScreenBody(navController = navController)
                    menus[1] -> GroupsScreenBody(navController = navController)
                    menus[2] -> RequestsScreenBody(navController = navController)
                    menus[3] -> SettingsScreenBody(navController = navController)
                }
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                menus.forEach { menu ->
                    UiTabItem(
                        modifier = Modifier
                            .weight(1f),
                        textLabel = menu.textLabel,
                        iconPainter = menu.painter,
                        isSelected = menu == selectedMenu,
                        onClick = {
                            selectedMenu = menu
                            Log.d("selected menu", menu.textLabel)
                        }
                    )
                }
            }
        }
    }
}

data class TabData(
    var textLabel: String,
    var painter: Painter
)