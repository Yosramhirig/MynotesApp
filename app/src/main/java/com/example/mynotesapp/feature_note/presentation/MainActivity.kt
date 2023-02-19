package com.example.mynotesapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotesapp.ui.theme.MyNotesAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesAppTheme {

                DestinationsNavHost(navGraph = NavGraphs.root)

                /////////////////////////////////////////////////MyNotesAppTheme
            }
        }
    }
}
/////////////////////////////////////////////////MyNotesAppTheme


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyNotesAppTheme {

    }
}