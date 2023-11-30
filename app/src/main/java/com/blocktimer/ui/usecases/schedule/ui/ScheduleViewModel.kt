package com.blocktimer.ui.usecases.schedule.ui

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blocktimer.ui.theme.Black
import com.blocktimer.ui.theme.Gray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

open class ScheduleViewModel(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): ViewModel() {
    private val data = dataStore

    private val _isBlockClicked = MutableLiveData<Boolean>(false)
    val isBlockClicked: LiveData<Boolean> = _isBlockClicked

    private val _blockColor = MutableLiveData<Color?>(null)
    val blockColor: LiveData<Color?> = _blockColor

    private val _blockCounter = MutableLiveData<Int>(0)
    val blockCounter: LiveData<Int> = _blockCounter

    private val _date = MutableLiveData<String?>(null)
    val date: LiveData<String?> = _date

    private val _iconBlockCounter = MutableLiveData<Int>(0)
    val iconBlockCounter: LiveData<Int> = _iconBlockCounter

    private val _clearBlocks = MutableLiveData<Boolean>(false)
    val clearBlocks: LiveData<Boolean> = _clearBlocks

    private val _today = MutableLiveData<LocalDate>(LocalDate.now())
    val today: LiveData<LocalDate> = _today

    private val _currentBlockIcon = MutableLiveData<String?>(null)
    val currentBlockIcon: LiveData<String?> = _currentBlockIcon

    private val _iconMap = MutableLiveData<MutableList<MutableList<Int>>>(mutableListOf(
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
    val iconMap: LiveData<MutableList<MutableList<Int>>> = _iconMap

    private val _nameMap = MutableLiveData<MutableList<MutableList<String?>>>(mutableListOf(
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
    val nameMap: LiveData<MutableList<MutableList<String?>>> = _nameMap

    private val _currentName = MutableLiveData<String?>(null)
    val currentName: LiveData<String?> = _currentName

    private val _iconCoords = MutableLiveData<MutableList<Pair<Int, Int>>>(mutableListOf())
    val iconCoords: LiveData<MutableList<Pair<Int, Int>>> = _iconCoords

    private val _iconId = MutableLiveData<Int>(0)
    val iconId: LiveData<Int> = _iconId

    private val _iconMapsCache = MutableLiveData<Map<String, MutableList<MutableList<Int>>>>(emptyMap())
    val iconMapsCache: LiveData<Map<String, MutableList<MutableList<Int>>>> = _iconMapsCache

    private val _nameMapsCache = MutableLiveData<Map<String, MutableList<MutableList<String?>>>>(emptyMap())
    val nameMapsCache: LiveData<Map<String, MutableList<MutableList<String?>>>> = _nameMapsCache

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private val _currentHour = MutableLiveData<LocalTime>(LocalTime.now())
    val currentHour: LiveData<LocalTime> = _currentHour

    init {
        startHourUpdate()
    }

    fun onTaskNameChange(taskName: String) {
        _taskName.value = taskName
    }

    fun resetBlockCounter() {
        _blockCounter.value = 0
    }

    fun updateDate(date: String?) {
        _date.value = date
    }

    fun onBlockClicked(isFocused: Boolean, blockRow: Int, blockColumn: Int) {
        _clearBlocks.value = false

        if(isFocused) {
            _blockCounter.value = _blockCounter.value!! + 1
            _iconCoords.value?.add(Pair(blockRow, blockColumn))
        } else {
            _blockCounter.value = _blockCounter.value!! - 1
            _iconCoords.value?.remove(Pair(blockRow, blockColumn))
        }

        _isBlockClicked.value = _blockCounter.value!! > 0
    }

    fun refreshDate(date: LocalDate) {
        _today.value = date
    }

    fun onTaskIconClicked() {
        _isBlockClicked.value = false
        _clearBlocks.value = true
        _currentName.value = _taskName.value

        for(i in _iconCoords.value!!) {
            setName(i.first, i.second, _currentName.value)
        }

        iconCoords.value?.clear()
    }

    fun isIconClicked(blockNumber: Int): Boolean {
       return _iconBlockCounter.value!! > 0 && _iconBlockCounter.value == blockNumber
    }

    fun onIconClicked(icon: String, localIconId: Int) {
        _iconBlockCounter.value = _iconBlockCounter.value!! + 1
        _currentBlockIcon.value = icon
        _iconId.value = localIconId
    }

    fun updateBlockNumber(): Int? {
       return _iconBlockCounter.value
    }

    fun updateIcons(icons: List<String>, icon: String) {
        for(i in _iconCoords.value!!) {
            setIcon(i.first, i.second, icons.indexOf(icon)+1)
        }
    }

    private fun setName(blockRow: Int, blockColumn: Int, currentText: String?) {
        _nameMap.value?.get(blockRow)?.set(blockColumn, currentText)
    }

    fun setIcon(blockRow: Int, blockColumn: Int, currentIcon: Int) {
        _iconMap.value?.get(blockRow)?.set(blockColumn, currentIcon)
    }

    fun saveMaps(date: String) {
        _iconMapsCache.value = (_iconMapsCache.value ?: emptyMap()).toMutableMap().apply {
            this[date] = _iconMap.value?.map { it.toMutableList() }?.toMutableList() ?: mutableListOf()
        }

        Log.i("mapsOperations", "saving map _iconMap: ${_iconMap.value}")
        Log.i("mapsOperations", "saving map _iconMap: ${_iconMapsCache.value?.get(date)}")

        _nameMapsCache.value = (_nameMapsCache.value ?: emptyMap()).toMutableMap().apply {
            this[date] = _nameMap.value?.map { it.toMutableList() }?.toMutableList() ?: mutableListOf()
        }
    }

    fun resetMaps() {
        _iconMap.value?.forEach { it.fill(0) }
        Log.i("mapsOperations", "resetmap _iconMap: ${_iconMap.value}")
        _nameMap.value?.forEach { it.fill(null) }
    }

    fun loadMaps(date: String) {
        _iconMapsCache.value?.get(date)?.let { nonNullMap ->
            _iconMap.value = nonNullMap.map { it.toMutableList() }.toMutableList()
            Log.i("mapsOperations", "loadmaps _iconMap: ${_iconMap.value}")
        }

        _nameMapsCache.value?.get(date)?.let { nonNullMap ->
            _nameMap.value = nonNullMap.map { it.toMutableList() }.toMutableList()
        }
    }

    fun updateIcon(blockRow: Int, blockColumn: Int): String? {
        return when (val value = _iconMap.value?.get(blockRow)?.get(blockColumn)) {
            in 1..19 -> "ic_$value"
            else -> null
        }
    }

    fun resetValues() {
        iconCoords.value?.clear()
        _isBlockClicked.value = false
    }

    fun setBlockColor(blockRow: Int, blockColumn: Int, currentHour: LocalTime, completedMap: MutableList<MutableList<Boolean>>): Color? {

        val mutableFormattedHour = derivedStateOf { currentHour.format(DateTimeFormatter.ofPattern("HH:mm")) }
        val hourIndex = mutableFormattedHour.value.substring(0, 2).toInt()
        val minuteIndex = mutableFormattedHour.value.substring(3).toInt() / 10

        Log.i("setBlockColor", "_today.value = ${_today.value}, LocalDate.now() = ${LocalDate.now()}")
        if(_today.value == LocalDate.now()) {
            if(blockRow < hourIndex || (blockRow <= hourIndex && blockColumn < minuteIndex)) {
                _blockColor.value = Gray
            }
            else if(completedMap[blockRow][blockColumn]) {
                _blockColor.value = Gray
            }
            else {
                _blockColor.value = Black
            }
        }
        else {
            _blockColor.value = Black
        }

        return _blockColor.value
    }

    private fun startHourUpdate() {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(TimeUnit.SECONDS.toMillis(30))

                _currentHour.value = LocalTime.now()
            }
        }
    }

    private suspend fun saveMapsData() {
        data.edit { preference ->
            _nameMapsCache.value?.forEach { (key, outerList) ->
                outerList.forEachIndexed { outerIndex, innerList ->
                    innerList.forEachIndexed { innerIndex, element ->
                        preference[stringPreferencesKey("nameMap[$key][$outerIndex][$innerIndex]")] =
                            element.orEmpty()
                    }
                }
            }

            _iconMapsCache.value?.forEach { (key, outerList) ->
                outerList.forEachIndexed { outerIndex, innerList ->
                    innerList.forEachIndexed { innerIndex, element ->
                        preference[intPreferencesKey("iconMap[$key][$outerIndex][$innerIndex]")] =
                            element
                    }
                }
            }
        }
    }

    private suspend fun recoverMapsData() {
        data.data.map {preferences ->
            preferences
        }
    }
}