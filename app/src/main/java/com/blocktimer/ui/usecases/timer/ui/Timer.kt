package com.blocktimer.ui.usecases.timer.ui


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.blocktimer.R
import com.blocktimer.ui.usecases.timer.ui.composables.AnalogClockComposable
import com.blocktimer.ui.theme.Black
import com.blocktimer.ui.theme.BlockTimerTheme
import com.blocktimer.ui.theme.White
import com.blocktimer.ui.usecases.schedule.ui.ScheduleViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimerScreen(navController: NavHostController, viewModel: ScheduleViewModel, secondViewModel: TimerViewModel) {
    val nameMapCache: Map<String, MutableList<MutableList<String?>>> by viewModel.nameMapsCache.observeAsState(emptyMap())
    val today = (LocalDate.now()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

    val taskTime: Pair<Int, Int> by secondViewModel.taskTime.observeAsState(Pair(0,0))
    val isTaskCompled: Boolean? by secondViewModel.isTaskCompleted.observeAsState(false)
    val actualTask: String? by secondViewModel.actualTask.observeAsState(null)

    //viewModel.resetValues()

    BlockTimerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Black
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // TODO --> HACER LOS SPACERS RESPONSIVES
                Clock()
                Timer(taskTime)
                Spacer(modifier = Modifier.height(70.dp))
                ActualActivity(nameMapCache[today], secondViewModel, actualTask)
                Spacer(modifier = Modifier.height(20.dp))
                CompletedButton(viewModel, secondViewModel, nameMapCache[today])
                Spacer(modifier = Modifier.height(10.dp))
                ScheduleButton(navController)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}


@Composable
fun Clock() {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        AnalogClockComposable()
    }
}

@Composable
fun Timer(taskTime: Pair<Int, Int>) {
    val h = taskTime.second
    val m = taskTime.first

    val totalSeconds = h * 3600 + m * 60
    var remainingSeconds by remember(taskTime) { mutableStateOf(totalSeconds) }

    LaunchedEffect(taskTime) {

        while (remainingSeconds > 0) {
            remainingSeconds--
            delay(1000)
        }
    }

    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    Text(
        text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.majormono_regular)),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = White
        )
    )
}

@Composable
fun ActualActivity(nameMap: MutableList<MutableList<String?>>?, secondViewModel: TimerViewModel, actualTask: String?) {
    secondViewModel.getCurrentTask(nameMap)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .height(100.dp)
    ) {
        Text(
            text = "-- Currently doing --",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.sometypemono_regular)),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        Log.i("prueba", "$actualTask")

        Text(
            text = modifyString(if(actualTask.isNullOrEmpty() || actualTask == "null")  "No activity is scheduled for now" else actualTask),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.sometypemono_regular)),
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                color = White
            )
        )
    }
}

@Composable
fun CompletedButton(viewModel: ScheduleViewModel, secondViewModel: TimerViewModel, nameMap: MutableList<MutableList<String?>>?, ) {
    Button(
        onClick = {
            secondViewModel.setCompleted(nameMap)
        },
        border = BorderStroke(1.dp, White),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Black),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(start = 45.dp, end = 45.dp)
    ) {
        Row() {
            Text(
                text = "Completed",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.sometypemono_regular)),
                    fontSize = 16.sp,
                    color = White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .offset(y = 2.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
    }
}

@Composable
fun ScheduleButton(nav: NavHostController) {
    Button(
        onClick = {
            nav.navigate("schedule")
        },
        border = BorderStroke(1.dp, White),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Black),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(start = 45.dp, end = 45.dp)
    ) {
        Row() {
            Text(
                text = "Schedule",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.sometypemono_regular)),
                    fontSize = 16.sp,
                    color = White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .offset(y = 2.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun TopButtons() {
    // TODO: Implementar luego de las cosas fundamentales
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview() {
    BlockTimerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Black
        ) {

        }
    }
}

fun modifyString(input: String): String {
    var modifiedString = input.trim()

    if (modifiedString.length > 66) {
        modifiedString = modifiedString.substring(0, 66) + "..."
    }

    modifiedString = modifiedString.lowercase(Locale.ROOT)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    return modifiedString
}

