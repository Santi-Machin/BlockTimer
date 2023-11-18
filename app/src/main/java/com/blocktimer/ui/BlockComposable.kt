package com.blocktimer.ui

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blocktimer.R
import com.blocktimer.ui.theme.Black
import com.blocktimer.ui.theme.Gray
import com.blocktimer.ui.theme.White
import java.lang.Math.ceil
import java.lang.Math.round
import kotlin.math.ceil
import kotlin.math.roundToInt


@Composable
fun Block(
    defaultColor: Color = Black,
    borderColor: Color = White,
    isFocused: Boolean = false,
    screenSize: Pair<Int, Int>,
    onBlockClicked: (Boolean) -> Unit,
    icon: String? = null,
    modifier: Modifier = Modifier,
)
{
    val density = LocalDensity.current
    val focusedColor = White
    val color = if (isFocused) focusedColor else defaultColor
    val blockParams = responsiveBoxSize(screenSize, density)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(blockParams.first.dp)
            .height(blockParams.second.dp)
            .clip(RoundedCornerShape(blockParams.third.dp))
            .background(
                color = color,
                shape = RoundedCornerShape(blockParams.third.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(blockParams.third.dp)
            )
            .clickable {
                onBlockClicked(!isFocused)
            }

    ) {
        if(!icon.isNullOrEmpty()) {
            val context: Context = LocalContext.current
            val resources: Resources = context.resources
            val resourceId = resources.getIdentifier(icon, "drawable", context.packageName)
            Log.i("resourceId", resourceId.toString())

            Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
                colorFilter = if(isFocused) ColorFilter.tint(defaultColor) else ColorFilter.tint(focusedColor)
            )
        }
    }
}

fun responsiveBoxSize(screenSize: Pair<Int, Int>, density: Density): Triple<Int, Int, Int> {
    var responsiveBoxParams = Triple(0,0,0)
    val screenWidth = with(density) { ceil(screenSize.first.toDp().value).toInt().coerceAtLeast(1) }

    Log.i("dps", "$screenWidth")

    /*
    First value -> width
    Second value -> height
    Third value -> RoundedCornerShape size
     */

    when {
        // TODO: ADD THE RESPOSIVENESS
        // 412
        screenWidth >= 412 ->
            responsiveBoxParams = Triple(45,45,15)

        else ->
            responsiveBoxParams = Triple(30,30, 10)
    }

    return responsiveBoxParams
}