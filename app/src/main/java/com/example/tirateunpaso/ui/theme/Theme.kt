import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.tirateunpaso.ui.theme.*

val LigthSurface = Color.White
val LightBlueCard = Color(0xFFE3F2FD)
val LightBlueGradientStart = Color(0xFF42A5F5)
val LightBlueGradientEnd = Color(0xFF1976D2)
// val BlueFrance = Color(0xFF01579B)
val DarkBlueFrance = Color(0xFF0D47A1)
val DarkBlue = Color(0xFF002366)
val DarkSurface = Color(0xFF1E1E1E)
val DarkBackground = Color(0xFF121212)

private val LightThemeColors = lightColorScheme(
    primary = LightBlueGradientStart,
    primaryContainer = LightBlueGradientStart,
    secondary = LightBlueGradientEnd,
    tertiary = DarkBlue,
    background = LightBlueCard,
    surface = LigthSurface,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = DarkBlue,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkThemeColors = darkColorScheme(
    primary = LightBlueGradientStart,
    primaryContainer = LightBlueGradientStart,
    secondary = DarkBlueFrance,
    tertiary = DarkBlue,
    background = DarkBackground,
    surface = DarkSurface,
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = DarkBlue,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun TirateUnPasoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
