package com.mathroda.dashcoin.presentation.dialog_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mathroda.dashcoin.presentation.ui.theme.LightGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite

@Composable
fun CustomDialogSignOut(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    onClick: () -> Unit

) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        CustomDialogUiSignOut(
            openDialogCustom = openDialogCustom,
            modifier = modifier,
        ) {
            onClick()
        }
    }
}


@Composable
fun CustomDialogUiSignOut(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        // modifier = modifier.size(280.dp, 240.dp)
        modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier
                .background(LightGray)) {

            //.......................................................................


            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Are You Sure",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h1,
                    maxLines = 2,
                    color = TextWhite,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "You want to logout ?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.body1,
                    color = TextWhite

                )
            }
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(LighterGray),
                horizontalArrangement = Arrangement.SpaceAround) {

                TextButton(onClick = {
                    openDialogCustom.value = false
                }) {

                    Text(
                        "Dismiss",
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(ContentAlpha.disabled),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                TextButton(onClick = {
                    onClick()
                }) {
                    Text(
                        "Yes",
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}