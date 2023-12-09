package com.blocktimer.ui.usecases.schedule.ui

import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.blocktimer.R
import com.blocktimer.ui.usecases.schedule.ui.composables.Block
import com.blocktimer.ui.theme.Black
import com.blocktimer.ui.theme.BlockTimerTheme
import com.blocktimer.ui.theme.Gray
import com.blocktimer.ui.theme.White
import com.blocktimer.ui.usecases.timer.ui.TimerViewModel
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Timer

var blockSetterHeight = mutableStateOf(0.dp)
var blockSetterWidth = mutableStateOf(0.dp)
var updateCounter by mutableIntStateOf(0)

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    secondViewModel: TimerViewModel,
    navController: NavHostController,
    screenParameters: Pair<Int, Int>,
    vibratorService: Vibrator?
) {
    // Observe the LiveData from ViewModel
    val isBlockClicked: Boolean by viewModel.isBlockClicked.observeAsState(false)
    val clearBlocks: Boolean by viewModel.clearBlocks.observeAsState(false)
    val isAnIcSelected: Boolean by viewModel.isAnIcSelected.observeAsState(false)
    val haveName: Boolean by viewModel.haveName.observeAsState(false)
    val nameMap: MutableList<MutableList<String?>> by viewModel.nameMap.observeAsState(mutableListOf(
        mutableListOf<String?>(null, null, null, null, null, null), // 00
        mutableListOf<String?>(null, null, null, null, null, null), // 01
        mutableListOf<String?>(null, null, null, null, null, null), // 02
        mutableListOf<String?>(null, null, null, null, null, null), // 03
        mutableListOf<String?>(null, null, null, null, null, null), // 04
        mutableListOf<String?>(null, null, null, null, null, null), // 05
        mutableListOf<String?>(null, null, null, null, null, null), // 06
        mutableListOf<String?>(null, null, null, null, null, null), // 07
        mutableListOf<String?>(null, null, null, null, null, null), // 08
        mutableListOf<String?>(null, null, null, null, null, null), // 09
        mutableListOf<String?>(null, null, null, null, null, null), // 10
        mutableListOf<String?>(null, null, null, null, null, null), // 11
        mutableListOf<String?>(null, null, null, null, null, null), // 12
        mutableListOf<String?>(null, null, null, null, null, null), // 13
        mutableListOf<String?>(null, null, null, null, null, null), // 14
        mutableListOf<String?>(null, null, null, null, null, null), // 15
        mutableListOf<String?>(null, null, null, null, null, null), // 16
        mutableListOf<String?>(null, null, null, null, null, null), // 17
        mutableListOf<String?>(null, null, null, null, null, null), // 18
        mutableListOf<String?>(null, null, null, null, null, null), // 19
        mutableListOf<String?>(null, null, null, null, null, null), // 20
        mutableListOf<String?>(null, null, null, null, null, null), // 21
        mutableListOf<String?>(null, null, null, null, null, null), // 22
        mutableListOf<String?>(null, null, null, null, null, null) // 23
    ))
    val iconMap: MutableList<MutableList<Int>> by viewModel.iconMap.observeAsState(mutableListOf(
        mutableListOf(0, 0, 0, 0, 0, 0), // 00
        mutableListOf(0, 0, 0, 0, 0, 0), // 01
        mutableListOf(0, 0, 0, 0, 0, 0), // 02
        mutableListOf(0, 0, 0, 0, 0, 0), // 03
        mutableListOf(0, 0, 0, 0, 0, 0), // 04
        mutableListOf(0, 0, 0, 0, 0, 0), // 05
        mutableListOf(0, 0, 0, 0, 0, 0), // 06
        mutableListOf(0, 0, 0, 0, 0, 0), // 07
        mutableListOf(0, 0, 0, 0, 0, 0), // 08
        mutableListOf(0, 0, 0, 0, 0, 0), // 09
        mutableListOf(0, 0, 0, 0, 0, 0), // 10
        mutableListOf(0, 0, 0, 0, 0, 0), // 11
        mutableListOf(0, 0, 0, 0, 0, 0), // 12
        mutableListOf(0, 0, 0, 0, 0, 0), // 13
        mutableListOf(0, 0, 0, 0, 0, 0), // 14
        mutableListOf(0, 0, 0, 0, 0, 0), // 15
        mutableListOf(0, 0, 0, 0, 0, 0), // 16
        mutableListOf(0, 0, 0, 0, 0, 0), // 17
        mutableListOf(0, 0, 0, 0, 0, 0), // 18
        mutableListOf(0, 0, 0, 0, 0, 0), // 19
        mutableListOf(0, 0, 0, 0, 0, 0), // 20
        mutableListOf(0, 0, 0, 0, 0, 0), // 21
        mutableListOf(0, 0, 0, 0, 0, 0), // 22
        mutableListOf(0, 0, 0, 0, 0, 0), // 23
    ))
    val taskName: String by viewModel.taskName.observeAsState("")
    val date: String? by viewModel.date.observeAsState("")
    val today: LocalDate by viewModel.today.observeAsState(LocalDate.now())
    val currentHour by viewModel.currentHour.observeAsState(LocalTime.now())
    val completedMap: MutableList<MutableList<Boolean>> by secondViewModel.completedMap.observeAsState(mutableListOf(
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
        mutableListOf<Boolean>(false, false, false, false, false, false), // 00
    ))

    BlockTimerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Black
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Date(viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                Blocks(
                    viewModel = viewModel,
                    screenParameters = screenParameters,
                    vibratorService = vibratorService,
                    clearBlocks = clearBlocks,
                    isBlockClicked = isBlockClicked,
                    currentHour = currentHour,
                    currentDay = today,
                    completedMap = completedMap
                )
            }
            if(isBlockClicked) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .padding(start = 10.dp , end = 10.dp, bottom = 10.dp)
                ) {
                    ColorMenu(
                        viewModel = viewModel,
                        screenParameters = screenParameters,
                        taskName = taskName,
                        date = date,
                        onTaskNameChange = { viewModel.onTaskNameChange(it) },
                        onTaskIconClicked = { viewModel.onTaskIconClicked() },
                        isAnIcSelected = isAnIcSelected,
                        haveName = haveName
                    )
                }
            }
        }
    }
}

val hours = listOf<String>(
    "00",
    "01",
    "02",
    "03",
    "04",
    "05",
    "06",
    "07",
    "08",
    "09",
    "10",
    "11",
    "12",
    "13",
    "14",
    "15",
    "16",
    "17",
    "18",
    "19",
    "20",
    "21",
    "22",
    "23"
)

@Composable
fun Date(
    viewModel: ScheduleViewModel
) {
    var mutableDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val mutableFormattedDate by remember(mutableDate) {
        derivedStateOf {
            mutableDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        }
    }

    //viewModel.loadMaps(date = mutableFormattedDate)
    viewModel.updateDate(mutableFormattedDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chevron_left),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.CenterVertically) // Center vertically
                .clickable {
                    viewModel.saveMaps(date = mutableFormattedDate)
                    mutableDate = mutableDate.minusDays(1)
                    viewModel.refreshDate(mutableDate)
                    viewModel.resetMaps()
                    viewModel.loadMaps(date = mutableFormattedDate)
                    viewModel.updateDate(mutableFormattedDate)
                    updateCounter++
                }
        )

        Text(
            text = mutableFormattedDate,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.majormono_regular)),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = White,
            ),
        )

        Image(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically) // Center vertically
                .clickable {
                    viewModel.saveMaps(date = mutableFormattedDate)
                    mutableDate = mutableDate.plusDays(1)
                    viewModel.refreshDate(mutableDate)
                    viewModel.resetMaps()
                    viewModel.loadMaps(date = mutableFormattedDate)
                    viewModel.updateDate(mutableFormattedDate)
                    updateCounter++
                }
        )
    }
}

@Composable
fun Blocks(
    viewModel: ScheduleViewModel,
    screenParameters: Pair<Int, Int>,
    vibratorService: Vibrator?,
    clearBlocks: Boolean,
    isBlockClicked: Boolean,
    currentHour: LocalTime,
    currentDay: LocalDate,
    completedMap: MutableList<MutableList<Boolean>>
) {
    Box(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            for(hour in hours) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp, top = 5.dp, start = 20.dp)
                ) {

                    Log.i("recomposicion", "a")
                    Text(
                        text = hour,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.majormono_regular)),
                            fontSize = 24.sp,
                            color = White,
                        ),
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .align(Alignment.CenterVertically)
                    )

                    repeat(6) {
                        var color by remember { mutableStateOf(Black) }
                        var isFocused by remember { mutableStateOf(false) }
                        val blockRow = hours.indexOf(hour)
                        val blockColumn = it

                        if(clearBlocks) {
                            isFocused = false
                            viewModel.resetBlockCounter()
                        }

                        Block(
                            defaultColor = color,
                            borderColor = White,
                            isFocused = isFocused,
                            screenSize = screenParameters,
                            icon = viewModel.updateIcon(blockRow, blockColumn),
                            onBlockClicked = {
                                isFocused = !isFocused
                                vibrate(vibratorService)
                                viewModel.onBlockClicked(isFocused, blockRow, blockColumn)
                            },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                        LaunchedEffect(currentHour, currentDay) {
                            color = viewModel.setBlockColor(blockRow, blockColumn, currentHour, completedMap)!!
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }

            if(isBlockClicked) {
                Spacer(modifier = Modifier.height(blockSetterHeight.value + 20.dp))
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }

            LaunchedEffect(updateCounter) {
                // This effect will be recomposed whenever updateCounter changes.
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorMenu(
    viewModel: ScheduleViewModel,
    screenParameters: Pair<Int, Int>,
    taskName : String,
    date: String?,
    onTaskNameChange: (String) -> Unit,
    onTaskIconClicked: () -> Unit,
    isAnIcSelected: Boolean,
    haveName: Boolean
) {
    val icons = (1..19).map { "ic_$it" }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .background(
                color = Black,
                shape = RoundedCornerShape(15.dp)
            )
            .border(
                width = 1.dp,
                color = White,
                shape = RoundedCornerShape(15.dp)
            )
            .onGloballyPositioned {
                blockSetterHeight.value = with(density) {
                    it.size.height.toDp()
                }

                blockSetterWidth.value = with(density) {
                    it.size.width.toDp()
                }
            }
    ) {

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                colorFilter = ColorFilter.tint(if(isAnIcSelected && haveName) White  else Gray),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .size(30.dp)
                    .clickable {
                        if(isAnIcSelected && haveName) {
                            onTaskIconClicked()
                            if (date != null) {
                                viewModel.saveMaps(date)
                            }
                        }
                    }
            )
        }

        OutlinedTextField(
            value = taskName,
            onValueChange = { onTaskNameChange(it) },
            label = {
                Text(
                    text = "Task name",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.sometypemono_italic)),
                        fontSize = 16.sp,
                        color = White,
                    ),
                )
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 5.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = White,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sometypemono_regular)),
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = White,
                unfocusedBorderColor = White
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .horizontalScroll(scrollState)
        ) {
            for(icon in icons) {
                var blockNumber by remember { mutableStateOf(0) }
                val localIconId = (icons.indexOf(icon)) + 1

                Block(
                    defaultColor = Black,
                    borderColor = White,
                    isFocused = viewModel.isIconClicked(blockNumber),
                    screenSize = screenParameters,
                    icon = icon,
                    onBlockClicked = {
                        viewModel.onIconClicked(icon, localIconId)
                        blockNumber = viewModel.updateBlockNumber()!!
                        viewModel.updateIcons(icons, icon)
                    },
                )
                Spacer(modifier = Modifier.width(5.dp))

            }
        }
    }
}

fun vibrate(vibratorService: Vibrator?) {
    vibratorService!!.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
}
