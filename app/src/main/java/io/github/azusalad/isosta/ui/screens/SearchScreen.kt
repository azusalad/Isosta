package io.github.azusalad.isosta.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.ui.components.UserCard

@Composable
fun SearchScreen(
    userList: List<IsostaUser>,
    onKeyboardDone: (String) -> Unit,
    onUserButtonClicked: (IsostaUser) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable {mutableStateOf("")}
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search for a user") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone(query) }
            ),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(modifier = modifier) {
            items(userList) { isostaUser ->
                UserCard(
                    user = isostaUser,
                    onUserButtonClicked = onUserButtonClicked
                )
                Spacer(modifier = Modifier.height(20.dp))
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
    SearchScreen(
        userList = userList,
        onKeyboardDone = {},
        onUserButtonClicked = {}
    )
}
