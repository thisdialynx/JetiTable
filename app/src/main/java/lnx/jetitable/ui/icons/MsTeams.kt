package lnx.jetitable.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val MsTeams: ImageVector
    get() {
        if (_MsTeams != null) {
            return _MsTeams!!
        }
        _MsTeams = ImageVector.Builder(
            name = "MsTeams",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(12.5f, 2f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.709f, 6.094f)
                curveTo(9.48f, 6.038f, 9.246f, 6f, 9f, 6f)
                lineTo(4f, 6f)
                curveTo(2.346f, 6f, 1f, 7.346f, 1f, 9f)
                lineTo(1f, 14f)
                curveTo(1f, 15.654f, 2.346f, 17f, 4f, 17f)
                lineTo(9f, 17f)
                curveTo(10.654f, 17f, 12f, 15.654f, 12f, 14f)
                lineTo(12f, 9f)
                curveTo(12f, 8.616f, 11.921f, 8.252f, 11.789f, 7.914f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.5f, 8f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.5f, 2f)
                close()
                moveTo(19f, 4f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19f, 8f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19f, 4f)
                close()
                moveTo(4.5f, 9f)
                lineTo(8.5f, 9f)
                curveTo(8.776f, 9f, 9f, 9.224f, 9f, 9.5f)
                curveTo(9f, 9.776f, 8.776f, 10f, 8.5f, 10f)
                lineTo(7f, 10f)
                lineTo(7f, 14f)
                curveTo(7f, 14.276f, 6.776f, 14.5f, 6.5f, 14.5f)
                curveTo(6.224f, 14.5f, 6f, 14.276f, 6f, 14f)
                lineTo(6f, 10f)
                lineTo(4.5f, 10f)
                curveTo(4.224f, 10f, 4f, 9.776f, 4f, 9.5f)
                curveTo(4f, 9.224f, 4.224f, 9f, 4.5f, 9f)
                close()
                moveTo(15f, 9f)
                curveTo(14.448f, 9f, 14f, 9.448f, 14f, 10f)
                lineTo(14f, 14f)
                curveTo(14f, 16.761f, 11.761f, 19f, 9f, 19f)
                curveTo(8.369f, 19f, 8.034f, 19.756f, 8.461f, 20.221f)
                curveTo(9.465f, 21.314f, 10.903f, 22f, 12.5f, 22f)
                curveTo(15.24f, 22f, 17.529f, 20.04f, 17.939f, 17.32f)
                curveTo(17.979f, 17.05f, 18f, 16.78f, 18f, 16.5f)
                lineTo(18f, 11f)
                curveTo(18f, 9.9f, 17.1f, 9f, 16f, 9f)
                lineTo(15f, 9f)
                close()
                moveTo(20.889f, 9f)
                curveTo(20.323f, 9f, 19.871f, 9.466f, 19.891f, 10.031f)
                curveTo(19.964f, 12.093f, 20f, 16.5f, 20f, 16.5f)
                curveTo(20f, 16.618f, 19.975f, 16.859f, 19.936f, 17.148f)
                curveTo(19.813f, 18.048f, 20.86f, 18.653f, 21.559f, 18.072f)
                curveTo(22.44f, 17.34f, 23f, 16.237f, 23f, 15f)
                lineTo(23f, 11f)
                curveTo(23f, 9.9f, 22.1f, 9f, 21f, 9f)
                lineTo(20.889f, 9f)
                close()
            }
        }.build()

        return _MsTeams!!
    }

@Suppress("ObjectPropertyName")
private var _MsTeams: ImageVector? = null
