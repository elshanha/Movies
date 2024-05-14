package com.example.movies.login.sign_in

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    icon: Painter? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .aspectRatio(6f),
        colors = colors,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        if (icon != null) {
            Icon(painter = icon, contentDescription = null)
        }
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        Text(text = text, fontSize = 16.sp)
    }

}