package com.example.coadingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coadingchallenge.feature.ui.poddetail.PodcastDetailScreen
import com.example.coadingchallenge.feature.ui.podlist.PodsListScreen
import com.example.coadingchallenge.ui.theme.CoadingChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val PODCAST_LIST = "podcast_list"
        const val PODCAST_DETAIL = "podcast_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoadingChallengeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = PODCAST_LIST) {
                    composable(PODCAST_LIST) {
                        PodsListScreen(
                            modifier = Modifier.fillMaxSize(),
                            onPodCastClick = {
                                navController.navigate(PODCAST_DETAIL)
                            })
                    }
                    composable(PODCAST_DETAIL) {
                        PodcastDetailScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

