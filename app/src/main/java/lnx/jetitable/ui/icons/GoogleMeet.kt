package lnx.jetitable.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val GoogleMeet: ImageVector
    get() {
        if (_GoogleMeet != null) {
            return _GoogleMeet!!
        }
        _GoogleMeet = ImageVector.Builder(
            name = "GoogleMeet",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 50f,
            viewportHeight = 50f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(2f, 18f)
                lineTo(2f, 32f)
                lineTo(12f, 32f)
                lineTo(12f, 18f)
                close()
                moveTo(39f, 9f)
                verticalLineToRelative(4.31f)
                lineToRelative(-10f, 9f)
                verticalLineTo(16f)
                horizontalLineTo(14f)
                verticalLineTo(6f)
                horizontalLineToRelative(22f)
                curveTo(37.66f, 6f, 39f, 7.34f, 39f, 9f)
                close()
                moveTo(29f, 27.69f)
                lineToRelative(10f, 9f)
                verticalLineTo(41f)
                curveToRelative(0f, 1.66f, -1.34f, 3f, -3f, 3f)
                horizontalLineTo(14f)
                verticalLineTo(34f)
                horizontalLineToRelative(15f)
                verticalLineTo(27.69f)
                close()
                moveTo(12f, 34f)
                verticalLineToRelative(10f)
                horizontalLineTo(5f)
                curveToRelative(-1.657f, 0f, -3f, -1.343f, -3f, -3f)
                verticalLineToRelative(-7f)
                horizontalLineTo(12f)
                close()
                moveTo(12f, 6f)
                lineTo(12f, 16f)
                lineTo(2f, 16f)
                close()
                moveTo(29f, 25f)
                lineTo(39f, 16f)
                lineTo(39f, 34f)
                close()
                moveTo(49f, 9.25f)
                verticalLineToRelative(31.5f)
                curveToRelative(0f, 0.87f, -1.03f, 1.33f, -1.67f, 0.75f)
                lineTo(41f, 35.8f)
                verticalLineTo(14.2f)
                lineToRelative(6.33f, -5.7f)
                curveTo(47.97f, 7.92f, 49f, 8.38f, 49f, 9.25f)
                close()
            }
        }.build()

        return _GoogleMeet!!
    }

@Suppress("ObjectPropertyName")
private var _GoogleMeet: ImageVector? = null
