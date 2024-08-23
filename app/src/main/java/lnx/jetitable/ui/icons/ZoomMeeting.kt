package lnx.jetitable.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ZoomMeeting: ImageVector
    get() {
        if (_ZoomMeeting != null) {
            return _ZoomMeeting!!
        }
        _ZoomMeeting = ImageVector.Builder(
            name = "ZoomMeeting",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(15.656f, 3f)
                horizontalLineTo(8.344f)
                curveTo(5.397f, 3f, 3f, 5.398f, 3f, 8.344f)
                verticalLineToRelative(7.312f)
                curveTo(3f, 18.603f, 5.397f, 21f, 8.344f, 21f)
                horizontalLineToRelative(7.313f)
                curveTo(18.603f, 21f, 21f, 18.603f, 21f, 15.656f)
                verticalLineTo(8.344f)
                curveTo(21f, 5.398f, 18.603f, 3f, 15.656f, 3f)
                close()
                moveTo(14f, 14.301f)
                curveTo(14f, 14.687f, 13.687f, 15f, 13.301f, 15f)
                horizontalLineTo(9.188f)
                curveTo(7.98f, 15f, 7f, 14.02f, 7f, 12.812f)
                verticalLineTo(9.699f)
                curveTo(7f, 9.313f, 7.313f, 9f, 7.699f, 9f)
                horizontalLineToRelative(4.113f)
                curveTo(13.02f, 9f, 14f, 9.98f, 14f, 11.188f)
                verticalLineTo(14.301f)
                close()
                moveTo(17f, 15f)
                lineToRelative(-2f, -2f)
                verticalLineToRelative(-2f)
                lineToRelative(2f, -2f)
                verticalLineTo(15f)
                close()
            }
        }.build()

        return _ZoomMeeting!!
    }

@Suppress("ObjectPropertyName")
private var _ZoomMeeting: ImageVector? = null
