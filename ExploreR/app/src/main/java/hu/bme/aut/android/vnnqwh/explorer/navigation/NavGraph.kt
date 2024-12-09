package hu.bme.aut.android.vnnqwh.explorer.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService
import hu.bme.aut.android.vnnqwh.explorer.feature.attraction_details.AttractionDetailsScreen
import hu.bme.aut.android.vnnqwh.explorer.feature.attraction_favorites.FavoritesScreen
import hu.bme.aut.android.vnnqwh.explorer.feature.attraction_list.AttractionsListScreen
import hu.bme.aut.android.vnnqwh.explorer.feature.attraction_map.AttractionsMapScreen
import hu.bme.aut.android.vnnqwh.explorer.feature.auth.login.LoginScreen
import hu.bme.aut.android.vnnqwh.explorer.feature.auth.register.RegisterScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    authService : AuthService
) {
    NavHost(
        navController = navController,
        startDestination = if (authService.hasUser) Screen.AttractionsList.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Screen.AttractionsList.route)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.AttractionsList.route) {
            AttractionsListScreen(
                navController = navController,
                onSignOut = {
                    navController.popBackStack(
                        route = Screen.Login.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Login.route)
                },
                onMapsClicked = {
                    navController.navigate(Screen.AttractionsMapScreen.route)
                },
                onFavoritesClicked = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }
        composable(Screen.AttractionDetails.route) { backStackEntry ->
            val attractionId = backStackEntry.arguments?.getString("id")
            attractionId?.let { id ->
                AttractionDetailsScreen(
                    navController = navController,
                    attractionId = id
                )
            }
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                navController = navController,
            )
        }
        composable(Screen.AttractionsMapScreen.route) {
           AttractionsMapScreen(
               navController = navController
           )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Login.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Login.route)
                },
                onSuccess = {
                    navController.navigate(Screen.AttractionDetails.route)
                }
            )
        }
    }
}