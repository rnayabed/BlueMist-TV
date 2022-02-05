package com.anatame.pickaflix.ui.detail.Composables

import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.compose.ui.Primary

@Composable
fun EpisodeView(
    selectedIndex: MutableState<Int>,
    id: Int,
    episodeName: String,
    episodeDataID: String,
    onClick: (id: Int, epsDataID: String) -> Unit
) {


    Button(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (id == selectedIndex.value) Primary else Color.DarkGray),
        onClick = {
            onClick(id, episodeDataID)
        }
    ) {
        Text(
            text = "$episodeName",
            color = Color.White
        )
    }
}

