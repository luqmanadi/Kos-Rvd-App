package com.kosrvd.app.feature.complaint.presentation.list_keluhan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.complaint.domain.usecase.GetListKeluhanUseCase
import com.kosrvd.app.feature.complaint.domain.utils.TypeKeluhan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ListKeluhanEvent {
    data class NavigateToDetailKeluhan(val idKeluhan: String) : ListKeluhanEvent
}

sealed interface ListKeluhanActions {
    data object TryAgain : ListKeluhanActions
    data class NavigateToDetailKeluhan(val idKeluhan: String) : ListKeluhanActions
    data class ChooseTab(val tabIndex: Int) : ListKeluhanActions
}

@HiltViewModel
class ListKeluhanViewModel @Inject constructor(
    private val getListKeluhanUseCase: GetListKeluhanUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ListKeluhanUiState())
    val state = _state
        .onStart {
            loadKeluhanData(TypeKeluhan.WAITING_CONFIRMATION, tabIndex = 0, isInitialLoad = true)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListKeluhanUiState()
        )

    private val _events = Channel<ListKeluhanEvent>()
    val events = _events.receiveAsFlow()

    private val initialLoadedTabs = mutableSetOf<Int>()
    // Track active jobs untuk prevent duplicate flows
    private val activeJobs = mutableMapOf<Int, Job>()

    fun onActions(actions: ListKeluhanActions) {
        when (actions) {
            is ListKeluhanActions.NavigateToDetailKeluhan -> navigateToDetailKeluhan(actions.idKeluhan)
            is ListKeluhanActions.ChooseTab -> {
                if (actions.tabIndex != _state.value.selectedTab) {
                    updateSelectedTab(actions.tabIndex)

                    if (actions.tabIndex !in initialLoadedTabs) {
                        // Tab belum pernah di-load → load dengan loading
                        loadKeluhanData(getStatusFromTab(actions.tabIndex), actions.tabIndex, isInitialLoad = true)
                    }
                }
            }
            ListKeluhanActions.TryAgain -> tryAgain()
        }
    }

    private fun loadKeluhanData(statusFromTab: TypeKeluhan, tabIndex: Int, isInitialLoad: Boolean) {
        activeJobs[tabIndex]?.cancel()

        val job = viewModelScope.launch {
            // ✅ Show loading HANYA untuk initial load
            if (isInitialLoad) {
                updateTabState(tabIndex, getTabStateForIndex(tabIndex).copy(isLoading = true, loadError = null))
            }

            getListKeluhanUseCase(typeKeluhan = statusFromTab).collect { result ->
                when(result){
                    is Result.Error -> {
                        updateTabState(
                            tabIndex = tabIndex,
                            newState = getTabStateForIndex(tabIndex).copy(
                                isLoading = false,
                                loadError = result.error.message,
                                listKeluhan = emptyList()
                            )
                        )
                        initialLoadedTabs.remove(tabIndex)
                    }
                    is Result.Success -> {
                        updateTabState(
                            tabIndex = tabIndex,
                            newState = getTabStateForIndex(tabIndex).copy(
                                isLoading = false,
                                loadError = null,
                                listKeluhan = result.data
                            )
                        )
                        if (isInitialLoad) {
                            initialLoadedTabs.add(tabIndex)
                        }
                    }
                }
            }
        }
        activeJobs[tabIndex] = job
    }

    private fun tryAgain() {
        val currentTab = _state.value.selectedTab
        loadKeluhanData(getStatusFromTab(currentTab), currentTab, true)
    }

    private fun getStatusFromTab(tabIndex: Int): TypeKeluhan {
        return when(tabIndex){
            0 -> TypeKeluhan.WAITING_CONFIRMATION
            1 -> TypeKeluhan.PROCESS
            2 -> TypeKeluhan.COMPLETION
            else -> TypeKeluhan.WAITING_CONFIRMATION
        }
    }

    private fun getTabStateForIndex(tabIndex: Int): TabKeluhanUi {
        return when(tabIndex) {
            0 -> _state.value.listKeluhanMenungguKonfirmasi
            1 -> _state.value.listKeluhanSedangDiproses
            2 -> _state.value.listKeluhanSelesai
            else -> TabKeluhanUi()
        }
    }

    private fun updateTabState(tabIndex: Int, newState: TabKeluhanUi) {
        _state.update { currentState ->
            when(tabIndex) {
                0 -> currentState.copy(listKeluhanMenungguKonfirmasi = newState)
                1 -> currentState.copy(listKeluhanSedangDiproses = newState)
                2 -> currentState.copy(listKeluhanSelesai = newState)
                else -> currentState
            }
        }
    }


    private fun updateSelectedTab(tabIndex: Int) {
        _state.update { it.copy(selectedTab = tabIndex) }
    }

    private fun navigateToDetailKeluhan(idKeluhan: String) {
        viewModelScope.launch {
            _events.send(ListKeluhanEvent.NavigateToDetailKeluhan(idKeluhan))
        }
    }

}