package lnx.jetitable.core

interface Destinations {
    val route: String
}

object Auth: Destinations {
    override val route = "Auth"
}
object Home: Destinations {
    override val route = "Home"
}
object Settings: Destinations {
    override val route = "Settings"
}
object About: Destinations {
    override val route = "About"
}
object Loading: Destinations {
    override val route = "Loading"
}