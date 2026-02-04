import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable // em caso de erro

import androidx.compose.material.icons.Icons
// Imports necessários para CADA ícone específico que você está usando
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn

sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object List : Route
    @Serializable
    data object Map : Route
}
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Route)
{
    data object HomeButton :
        BottomNavItem("Início", Icons.Default.Home, Route.Home)
    data object ListButton :
        BottomNavItem("Favoritos", Icons.Default.Favorite, Route.List)
    data object MapButton :
        BottomNavItem("Mapa", Icons.Default.LocationOn, Route.Map)
}

