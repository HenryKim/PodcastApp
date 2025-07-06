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
import com.example.coadingchallenge.feature.PodCastViewModel
import com.example.coadingchallenge.feature.ui.poddetail.PodcastDetailScreen
import com.example.coadingchallenge.feature.ui.podlist.PodsListScreen
import com.example.coadingchallenge.ui.theme.CoadingChallengeTheme
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
        // Enables edge-to-edge display for a more immersive UI.
        enableEdgeToEdge()
        // Sets the content of the activity to be a Composable UI.
        setContent {
            CoadingChallengeTheme {
                // Surface is a basic building block in Compose, providing a background color.
                // Using fillMaxSize to ensure the theme and content occupy the entire screen.
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
    // Creates and remembers a NavController, which is responsible for managing app navigation.
    val navController = rememberNavController()
    // Retrieves an instance of PodCastViewModel using Hilt.
    // This ViewModel is shared between the podcast list and detail screens.
    val viewModel: PodCastViewModel = hiltViewModel()

    // NavHost is a container for defining navigation destinations.
    NavHost(navController = navController, startDestination = MainActivity.PODCAST_LIST) {
        // Defines the podcast list screen.
        composable(MainActivity.PODCAST_LIST) {
            PodsListScreen(
                // Modifier.fillMaxSize() ensures the screen takes up all available space.
                modifier = Modifier.fillMaxSize(),
                // Lambda triggered when a podcast item is clicked.
                onPodCastClick = { selectedPodcast ->
                    // Notifies the ViewModel about the selected podcast.
                    viewModel.onPodCastClick(selectedPodcast)
                    // Navigates to the podcast detail screen.
                    navController.navigate(MainActivity.PODCAST_DETAIL)
                }
            )
        }
        // Defines the podcast detail screen.
        composable(MainActivity.PODCAST_DETAIL) {
            PodcastDetailScreen(
                // Collects the highlighted podcast from the ViewModel as state.
                // `collectAsStateWithLifecycle` ensures collection is lifecycle-aware.
                podcast = viewModel.highlightedPodcast.collectAsStateWithLifecycle().value,
                // Lambda triggered when the favorite button is clicked (currently a no-op).
                onFavoriteClick = { /* TODO: Implement favorite functionality */ },
                // Lambda triggered when the back button is pressed.
                onBack = {
                    // Clears the currently highlighted podcast in the ViewModel.
                    viewModel.clearHighlightedPodcast()
                    // Navigates back to the previous screen in the back stack.
                    navController.popBackStack()
                }
            )
        }
    }
}
