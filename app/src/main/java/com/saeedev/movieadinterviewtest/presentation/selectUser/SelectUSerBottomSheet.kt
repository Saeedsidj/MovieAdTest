package com.saeedev.movieadinterviewtest.presentation.selectUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saeedev.movieadinterviewtest.R

@Composable
fun SelectUserBottomSheet(
    viewModel: SelectUserBottomSheetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val userList by viewModel.usersList.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(onCloseClick = onNavigateBack)
        UserList(
            userList = userList,
            onSelectUser = { id ->
                viewModel.selectNewUser(id)
                onNavigateBack()
            }
        )
    }
}


@Preview
@Composable
fun UserList(
    userList: List<SelectUser> = listOf(SelectUser("alex", 1), SelectUser("flux", 2)),
    onSelectUser: (Int) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(25, 19, 27))
            .padding(16.dp)
            .padding(bottom = 26.dp),
    ) {
        userList.forEach {
            UserCard(
                user = it,
                onUserClick = {
                    onSelectUser(it.id)
                }
            )
        }
    }
}

@Composable
fun UserCard(
    user: SelectUser,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(onClick = onUserClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "",
            tint = Color.White
        )
        Text(
            text = user.name,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

    }
}

@Preview
@Composable
fun TopBar(
    onCloseClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(52, 40, 56))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.users),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(
            onClick = onCloseClick
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                tint = Color.White
            )
        }
    }
}