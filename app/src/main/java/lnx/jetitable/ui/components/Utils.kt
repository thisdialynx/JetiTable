package lnx.jetitable.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

fun getColumnCardShape(index: Int, isLast: Boolean): RoundedCornerShape {
    val roundedCorner = 12.dp
    val semiRounded = 4.dp

    return when {
        index == 0 && !isLast -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }

        index != 0 && isLast -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }

        index != 0 && !isLast -> {
            RoundedCornerShape(semiRounded)
        }

        else -> {
            RoundedCornerShape(roundedCorner)
        }
    }
}

fun getRowCardShape(index: Int, isLast: Boolean): RoundedCornerShape {
    val roundedCorner = 12.dp
    val semiRounded = 4.dp

    return when {
        index == 0 && !isLast -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = semiRounded,
                bottomStart = roundedCorner,
                bottomEnd = semiRounded
            )
        }

        index != 0 && isLast -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = roundedCorner,
                bottomStart = semiRounded,
                bottomEnd = roundedCorner
            )
        }

        index != 0 && !isLast -> {
            RoundedCornerShape(semiRounded)
        }

        else -> {
            RoundedCornerShape(roundedCorner)
        }
    }
}