package com.example.coadingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coadingchallenge.presentation.ui.poddetail.PodcastDetailScreen
import com.example.coadingchallenge.presentation.ui.podlist.PodsListScreen
import com.example.coadingchallenge.ui.theme.CoadingChallengeTheme
import com.example.codingchallenge.feature.PodCastViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 * This activity serves as the entry point and hosts the Jetpack Compose UI.
 * It uses Hilt for dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Defines route constants for navigation within the app.
    companion object {
        const val PODCAST_LIST = "podcast_list"
        const val PODCAST_DETAIL = "podcast_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoadingChallengeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * Composable function responsible for setting up the navigation graph of the application.
 * It defines the different screens and how to navigate between them.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val viewModel: PodCastViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = MainActivity.PODCAST_LIST) {
        composable(MainActivity.PODCAST_LIST) {
            PodsListScreen(
                modifier = Modifier.fillMaxSize(),
                onPodCastClick = { selectedPodcast ->
                    // Notifies the ViewModel about the selected podcast.
                    viewModel.onPodCastClick(selectedPodcast)
                    navController.navigate(MainActivity.PODCAST_DETAIL)
                }
            )
        }

        composable(MainActivity.PODCAST_DETAIL) {
            PodcastDetailScreen(
                // Collects the highlighted podcast from the ViewModel as state.
                podcast = viewModel.highlightedPodcast.collectAsStateWithLifecycle().value,
                onFavoriteClick = { viewModel.toggleFavoritePodcast(it) },
                onBack = {
                    // Clears the currently highlighted podcast in the ViewModel.
                    viewModel.clearHighlightedPodcast()
                    navController.popBackStack()
                }
            )
        }
    }
}
