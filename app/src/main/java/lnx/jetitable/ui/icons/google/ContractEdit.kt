package lnx.jetitable.ui.icons.google

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ContractEdit: ImageVector
    get() {
        if (_ContractEdit != null) {
            return _ContractEdit!!
        }
        _ContractEdit = ImageVector.Builder(
            name = "ContractEdit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(360f, 360f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(80f)
                lineTo(360f, 360f)
                close()
                moveTo(360f, 480f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(80f)
                lineTo(360f, 480f)
                close()
                moveTo(480f, 800f)
                lineTo(200f, 800f)
                horizontalLineToRelative(280f)
                close()
                moveTo(480f, 880f)
                lineTo(240f, 880f)
                quadToRelative(-50f, 0f, -85f, -35f)
                reflectiveQuadToRelative(-35f, -85f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(-560f)
                horizontalLineToRelative(600f)
                verticalLineToRelative(361f)
                quadToRelative(-20f, -2f, -40.5f, 1.5f)
                reflectiveQuadTo(760f, 455f)
                verticalLineToRelative(-295f)
                lineTo(320f, 160f)
                verticalLineToRelative(480f)
                horizontalLineToRelative(240f)
                lineToRelative(-80f, 80f)
                lineTo(200f, 720f)
                verticalLineToRelative(40f)
                quadToRelative(0f, 17f, 11.5f, 28.5f)
                reflectiveQuadTo(240f, 800f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(80f)
                close()
                moveTo(560f, 880f)
                verticalLineToRelative(-123f)
                lineToRelative(221f, -220f)
                quadToRelative(9f, -9f, 20f, -13f)
                reflectiveQuadToRelative(22f, -4f)
                quadToRelative(12f, 0f, 23f, 4.5f)
                reflectiveQuadToRelative(20f, 13.5f)
                lineToRelative(37f, 37f)
                quadToRelative(8f, 9f, 12.5f, 20f)
                reflectiveQuadToRelative(4.5f, 22f)
                quadToRelative(0f, 11f, -4f, 22.5f)
                reflectiveQuadTo(903f, 660f)
                lineTo(683f, 880f)
                lineTo(560f, 880f)
                close()
                moveTo(860f, 617f)
                lineTo(823f, 580f)
                lineTo(860f, 617f)
                close()
                moveTo(620f, 820f)
                horizontalLineToRelative(38f)
                lineToRelative(121f, -122f)
                lineToRelative(-18f, -19f)
                lineToRelative(-19f, -18f)
                lineToRelative(-122f, 121f)
                verticalLineToRelative(38f)
                close()
                moveTo(761f, 679f)
                lineTo(742f, 661f)
                lineTo(779f, 698f)
                lineTo(761f, 679f)
                close()
            }
        }.build()

        return _ContractEdit!!
    }

@Suppress("ObjectPropertyName")
private var _ContractEdit: ImageVector? = null
