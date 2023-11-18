package com.blocktimer

import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val vibratorService = getSystemService(VIBRATOR_SERVICE) as Vibrator?

        setContent {
            val navController = rememberNavController()
            val screenParameters = ScreenSize(windowManager, displayManager)
            Log.i("screen_params", "Device width: ${screenParameters.first}, device height: ${screenParameters.second}")

            NavHost(
                navController = navController,
                startDestination = "timer"
            ) {

                composable("root") {
                    MainActivity()
                }

                composable("timer"){
                    TimerScreen(navController)
                }

                composable("schedule"){
                    ScheduleScreen(navController, screenParameters, vibratorService)
                }

            }
        }
    }
}

// TODO --> REMAKE ALL THIS SHIT AND MAKE REAL RESPONSIVENESS BASED ON OFFICIAL DOCS
@Composable
fun ScreenSize(
    windowManager: WindowManager,
    displayManager: DisplayManager
): Pair<Int, Int> {
    var deviceHeight = 0
    var deviceWidth = 0

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wMetrics = windowManager.currentWindowMetrics
        val bounds = wMetrics.bounds
        deviceHeight = bounds.height()
        deviceWidth = bounds.width()
    } else {
        val displayInfo =
            displayManager.getDisplay(
                Display.DEFAULT_DISPLAY
            )

        val realSize = Point()
        Display::class.java.getMethod("getRealSize", Point::class.java)
            .invoke(displayInfo, realSize)
        displayInfo.getRealSize(realSize)
        deviceWidth = realSize.x
        deviceHeight = realSize.y
    }

    return Pair(deviceWidth, deviceHeight)
}
