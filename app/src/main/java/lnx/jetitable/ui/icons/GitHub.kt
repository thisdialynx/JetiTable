package lnx.jetitable.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Github: ImageVector
    get() {
        if (_Github != null) {
            return _Github!!
        }
        _Github = ImageVector.Builder(
            name = "Github",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.9991f, 2f)
                curveTo(6.4771f, 2f, 2f, 6.4771f, 2f, 12.0003f)
                curveTo(2f, 16.4185f, 4.865f, 20.1663f, 8.8388f, 21.4892f)
                curveTo(9.3391f, 21.5807f, 9.5214f, 21.2719f, 9.5214f, 21.0067f)
                curveTo(9.5214f, 20.7698f, 9.5128f, 20.1405f, 9.5079f, 19.3062f)
                curveTo(6.7264f, 19.9103f, 6.1395f, 17.9655f, 6.1395f, 17.9655f)
                curveTo(5.6846f, 16.8102f, 5.0289f, 16.5026f, 5.0289f, 16.5026f)
                curveTo(4.121f, 15.8826f, 5.0977f, 15.8948f, 5.0977f, 15.8948f)
                curveTo(6.1014f, 15.9654f, 6.6294f, 16.9256f, 6.6294f, 16.9256f)
                curveTo(7.5213f, 18.4535f, 8.9701f, 18.0122f, 9.5398f, 17.7562f)
                curveTo(9.6307f, 17.1103f, 9.8885f, 16.6696f, 10.1746f, 16.4197f)
                curveTo(7.9541f, 16.1674f, 5.6195f, 15.3092f, 5.6195f, 11.4773f)
                curveTo(5.6195f, 10.3858f, 6.0093f, 9.4932f, 6.649f, 8.7939f)
                curveTo(6.5459f, 8.541f, 6.2027f, 7.5244f, 6.7466f, 6.1474f)
                curveTo(6.7466f, 6.1474f, 7.5864f, 5.8786f, 9.4969f, 7.1726f)
                curveTo(10.2943f, 6.9504f, 11.1501f, 6.8399f, 12.0003f, 6.8362f)
                curveTo(12.8493f, 6.8399f, 13.7051f, 6.9504f, 14.5038f, 7.1726f)
                curveTo(16.413f, 5.8786f, 17.2509f, 6.1474f, 17.2509f, 6.1474f)
                curveTo(17.7967f, 7.5244f, 17.4535f, 8.541f, 17.3504f, 8.7939f)
                curveTo(17.9913f, 9.4932f, 18.3786f, 10.3858f, 18.3786f, 11.4773f)
                curveTo(18.3786f, 15.319f, 16.0403f, 16.1643f, 13.8125f, 16.4117f)
                curveTo(14.1716f, 16.7205f, 14.4915f, 17.3307f, 14.4915f, 18.2638f)
                curveTo(14.4915f, 19.6003f, 14.4792f, 20.6789f, 14.4792f, 21.0067f)
                curveTo(14.4792f, 21.2744f, 14.6591f, 21.5856f, 15.1668f, 21.488f)
                curveTo(19.1374f, 20.1626f, 22f, 16.4173f, 22f, 12.0003f)
                curveTo(22f, 6.4771f, 17.5223f, 2f, 11.9991f, 2f)
                close()
            }
        }.build()

        return _Github!!
    }

@Suppress("ObjectPropertyName")
private var _Github: ImageVector? = null
