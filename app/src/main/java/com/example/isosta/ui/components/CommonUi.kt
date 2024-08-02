package com.example.isosta.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun TextMessageScreen(
    text: String,
    modifier: Modifier = Modifier
) {
    println("LOG: Loading text message screen with the text: " + text)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            //fontSize = 30.sp,
            //lineHeight = 75.sp,
            //color = Color.Blue
        )
    }
}

@Preview
@Composable
fun TextMessageScreenPreview() {
    TextMessageScreen(
        text = """
            FATAL EXCEPTION: main
            Process: com.example.isosta, PID: 26421
            java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                at java.util.ArrayList.get(ArrayList.java:437)
                                                                                           
        """.trimIndent()
    )
}
