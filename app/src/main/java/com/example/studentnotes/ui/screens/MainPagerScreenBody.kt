package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.ui.theme.Blue500
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun MainPagerScreenBody(
    navController: NavController
) {
    val tabs = listOf(
        TabItem(R.drawable.ic_events_24) { EventsScreenBody(navController) },
        TabItem(R.drawable.ic_groups_24) { GroupsScreenBody(navController) },
        TabItem(R.drawable.ic_requests_24) { RequestsScreenBody(navController) },
        TabItem(R.drawable.ic_settings_24) { SettingsScreenBody(navController) }
    )
    val pagerState = rememberPagerState(pageCount = tabs.size)
    Scaffold {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                TabsContent(
                    tabs = tabs,
                    pagerState = pagerState
                )
            }
            Tabs(
                tabs = tabs,
                pagerState = pagerState
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        tabs[page].screen()
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun Tabs(
    tabs: List<TabItem>,
    pagerState: PagerState,

) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Blue500,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = null) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

typealias ComposableFun = @Composable () -> Unit

data class TabItem(var icon: Int, var screen: ComposableFun)