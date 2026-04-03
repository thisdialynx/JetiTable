package lnx.jetitable.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

fun getCardShape(index: Int, isLastElement: Boolean): RoundedCornerShape {
    val roundedCorner = 12.dp
    val semiRounded = 4.dp

    return when {
        index == 0 && !isLastElement -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }

        index != 0 && isLastElement -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }

        index != 0 && !isLastElement -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }

        else -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }
    }
}