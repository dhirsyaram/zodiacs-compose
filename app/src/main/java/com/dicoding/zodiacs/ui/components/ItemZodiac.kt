package com.dicoding.zodiacs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.zodiacs.R
import com.dicoding.zodiacs.ui.theme.ZodiacsTheme

@Composable
fun ItemZodiac(
    itemId: Int,
    name: String,
    photoUrl: Int,
    birthdate: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(120.dp)
            .width(420.dp)
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(photoUrl),
            contentDescription = "image_zodiacs",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = birthdate,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemZodiac() {
    ZodiacsTheme {
        ItemZodiac(itemId = 3, name = "Aries", photoUrl = R.drawable.icon_aries, birthdate = "Mar 21 - Apr 19")
    }
}