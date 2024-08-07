package io.github.azusalad.isosta.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.ui.components.TextMessageScreen
import io.github.azusalad.isosta.ui.components.UserCard

@Composable
fun SearchScreen(
    isostaSearchUiState: IsostaSearchUiState,
    onKeyboardDone: (String) -> Unit,
    onUserButtonClicked: (IsostaUser) -> Unit,
    modifier: Modifier = Modifier
) {
    // Switch statement shows different things depending on the IsostaSearchUiState
    when (isostaSearchUiState) {
        is IsostaSearchUiState.Empty -> SearchColumn(
            userList = arrayListOf<IsostaUser>(),
            onKeyboardDone = onKeyboardDone,
            onUserButtonClicked = onUserButtonClicked
        )
        is IsostaSearchUiState.Loading -> {
            TextMessageScreen(text = "Searching", modifier = modifier.fillMaxSize())
        }
        is IsostaSearchUiState.Success -> SearchColumn(
            userList = isostaSearchUiState.userList,
            onKeyboardDone = onKeyboardDone,
            onUserButtonClicked = onUserButtonClicked,
        )
        is IsostaSearchUiState.Error -> TextMessageScreen(
            text = "There was a error making the search:\n\n" + isostaSearchUiState.errorString,
            modifier = modifier.fillMaxSize())
    }
}

@Composable
fun SearchColumn(
    userList: List<IsostaUser>,
    onKeyboardDone: (String) -> Unit,
    onUserButtonClicked: (IsostaUser) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable {mutableStateOf("")}
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search for a user") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done  // When this is search the search is not made?
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone(query) }
                ),
                modifier = Modifier.padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = 0.dp,
                    end = 4.dp
                )
            )
            FloatingActionButton(
                onClick = { onKeyboardDone(query) },
                modifier = Modifier.padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = 4.dp,
                    end = 0.dp
                )
            ) {
                Icon(Icons.Default.Search, "Search Button")
            }
        }
        LazyColumn(modifier = modifier) {
            items(userList) { isostaUser ->
                UserCard(
                    user = isostaUser,
                    onUserButtonClicked = onUserButtonClicked
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    val yui = "https://avatars.githubusercontent.com/u/68360714?v=4"
    val userList = arrayListOf<IsostaUser>()
    userList.add(
        IsostaUser(
            profilePicture = yui,
            profileName = "Yui",
            profileHandle = "@yui",
            profileLink = "",
        )
    )
    userList.add(
        IsostaUser(
            profilePicture = yui,
            profileName = "Yui2",
            profileHandle = "@yui2",
            profileLink = "",
        )
    )
    SearchColumn(
        userList = userList,
        onKeyboardDone = {},
        onUserButtonClicked = {}
    )
}
