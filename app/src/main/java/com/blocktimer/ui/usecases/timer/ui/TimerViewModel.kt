package com.blocktimer.ui.usecases.timer.ui

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimerViewModel: ViewModel() {
    private val _taskTime = MutableLiveData<Pair<Int, Int>>(Pair(0,0))
    val taskTime: LiveData<Pair<Int, Int>> = _taskTime

    private val _actualTask = MutableLiveData<String>("")
    val actualTask: LiveData<String?> = _actualTask

    private val _isTaskCompleted = MutableLiveData<Boolean>(false)
    val isTaskCompleted: LiveData<Boolean> = _isTaskCompleted

    private val _completedMap = MutableLiveData<MutableList<MutableList<Boolean>>>(mutableListOf(
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
    val completedMap: LiveData<MutableList<MutableList<Boolean>>> = _completedMap

    private val _mutableHour = mutableStateOf(LocalTime.now())
    private val _mutableFormattedHour = derivedStateOf {
        _mutableHour.value.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
    private val _hourIndex = _mutableFormattedHour.value.substring(0, 2).toInt()
    private val _minuteIndex = _mutableFormattedHour.value.substring(3).toInt() / 10
    private var _exactMinuteIndex = _mutableFormattedHour.value.substring(4).toInt()

    private fun setTaskTime(nameMap: MutableList<MutableList<String?>>?) {
        clearTaskTime()

        if (nameMap != null) {
            Log.i("setTime", "namemap = $nameMap")

            for ((index, list) in nameMap.withIndex()) {
                if(index >= _hourIndex) {
                    for((secondIndex, name) in list.withIndex()) {
                        if(secondIndex >= _minuteIndex) {
                            if (_actualTask.value == name && _actualTask.value != "null") {
                                if (_taskTime.value!!.first < 61) {
                                    _taskTime.value = Pair(_taskTime.value!!.first + (10-_exactMinuteIndex), _taskTime.value!!.second)
                                    _exactMinuteIndex = 0
                                    Log.i("setTime", "+10 minutos: _tasktime por tarea $name = ${_taskTime.value}")
                                } else {
                                    _taskTime.value = Pair(_taskTime.value!!.first - 60, _taskTime.value!!.second + 1)
                                    Log.i("setTime", "+1 hora por tarea $name: _tasktime = ${_taskTime.value}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clearTaskTime() {
        _taskTime.value = Pair(0,0)
    }

    private fun setActualTask(actualTask: String?, nameMap: MutableList<MutableList<String?>>?) {
        Log.i("bucle", "Soy un bucle infinito de setActualTask invocado por getCurrentTask")
        if (actualTask != null) {
            _actualTask.value = actualTask
            setTaskTime(nameMap)
        } else {
            _actualTask.value = null
        }
    }

    fun getCurrentTask(nameMap: MutableList<MutableList<String?>>?) {
        Log.i("bucle", "Soy un bucle infinito de getCurrentTask")
        setActualTask(nameMap?.get(_hourIndex)?.get(_minuteIndex), nameMap)
        //return nameMap?.get(_hourIndex)?.get(_minuteIndex)
    }

    fun setCompleted(nameMap: MutableList<MutableList<String?>>?) {
        _isTaskCompleted.value = _isTaskCompleted.value?.not()
        nameMap?.get(_hourIndex)?.set(_minuteIndex, null)
        _completedMap.value?.get(_hourIndex)?.set(_minuteIndex, true)
        _actualTask.value = null
        _taskTime.value = Pair(0,0)
    }


}