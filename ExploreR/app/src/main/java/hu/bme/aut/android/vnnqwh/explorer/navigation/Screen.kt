package hu.bme.aut.android.vnnqwh.explorer.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object AttractionsList : Screen("attractions_list")
    object AttractionDetails : Screen("attraction_details/{id}") {
        fun passId(id: String): String = "attraction_details/$id"
    }
    object Favorites : Screen("favorites")
    object AttractionsMapScreen : Screen("attractions_map")
    object Register : Screen("register")
}