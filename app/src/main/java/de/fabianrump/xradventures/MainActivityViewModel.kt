package de.fabianrump.xradventures

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivityViewModel: ViewModel() {

    private val fishItems = createFishItems()

    private val _uiState = MutableStateFlow(
        value = MainActivityUiState(
            fishItems = fishItems,
            selectedFishItemIndex = 0
        )
    )
    val uiState = _uiState.asStateFlow()

    fun updateSelectedFishItemIndex(selectedFishItemIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedFishItemIndex = selectedFishItemIndex)
        }
    }
}