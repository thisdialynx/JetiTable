package lnx.jetitable.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Moodle: ImageVector
    get() {
        if (_Moodle != null) {
            return _Moodle!!
        }
        _Moodle = ImageVector.Builder(
            name = "Moodle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(14f, 3f)
                lineTo(6f, 4f)
                lineTo(0f, 8f)
                lineTo(1f, 8f)
                lineTo(1f, 18f)
                lineTo(2f, 18f)
                lineTo(2f, 8f)
                lineTo(4.012f, 8f)
                curveTo(4.008f, 8.066f, 4f, 8.125f, 4f, 8.195f)
                curveTo(4f, 9.379f, 4.32f, 10.199f, 4.32f, 10.199f)
                lineTo(8.766f, 11.262f)
                lineTo(12.016f, 7.586f)
                curveTo(12.016f, 7.586f, 11.719f, 6.359f, 11.047f, 5.461f)
                close()
                moveTo(18.5f, 7f)
                curveTo(16.93f, 7f, 15.508f, 7.676f, 14.5f, 8.742f)
                curveTo(14.242f, 8.469f, 13.961f, 8.215f, 13.652f, 8f)
                lineTo(11.633f, 10.281f)
                curveTo(12.441f, 10.699f, 13f, 11.531f, 13f, 12.5f)
                lineTo(13f, 20f)
                lineTo(16f, 20f)
                lineTo(16f, 12.5f)
                curveTo(16f, 11.102f, 17.102f, 10f, 18.5f, 10f)
                curveTo(19.898f, 10f, 21f, 11.102f, 21f, 12.5f)
                lineTo(21f, 20f)
                lineTo(24f, 20f)
                lineTo(24f, 12.5f)
                curveTo(24f, 9.48f, 21.52f, 7f, 18.5f, 7f)
                close()
                moveTo(5.031f, 11.91f)
                curveTo(5.012f, 12.105f, 5f, 12.301f, 5f, 12.5f)
                lineTo(5f, 20f)
                lineTo(8f, 20f)
                lineTo(8f, 12.621f)
                close()
            }
        }.build()

        return _Moodle!!
    }

@Suppress("ObjectPropertyName")
private var _Moodle: ImageVector? = null
