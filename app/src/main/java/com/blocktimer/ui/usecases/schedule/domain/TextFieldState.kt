package com.blocktimer.ui.usecases.schedule.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TextFieldState {
    var text: String by mutableStateOf("")
}