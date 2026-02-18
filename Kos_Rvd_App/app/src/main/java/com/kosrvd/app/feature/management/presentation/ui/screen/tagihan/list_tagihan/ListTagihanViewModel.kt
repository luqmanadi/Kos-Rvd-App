package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.list_tagihan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.usecase.GetListTagihanUseCase
import com.kosrvd.app.feature.management.domain.utils.TypeTagihan
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

sealed interface ListTagihanEvent {
    data class NavigateToDetailTagihan(val idTagihan: String) : ListTagihanEvent
}

sealed interface ListTagihanActions {
    data object TryAgain : ListTagihanActions
    data class NavigateToDetailTagihan(val idTagihan: String) : ListTagihanActions
    data class ChooseTab(val tabIndex: Int) : ListTagihanActions
}

@HiltViewModel
class ListTagihanViewModel @Inject constructor(
    private val getListTagihanUseCase: GetListTagihanUseCase
): ViewModel() {
    private val _state = MutableStateFlow(ListTagihanUiState())
    val state = _state
        .onStart {
            loadTagihanData(TypeTagihan.UNPAID, tabIndex = 0, isInitialLoad = true)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListTagihanUiState()
        )

    private val _events = Channel<ListTagihanEvent>()
    val events = _events.receiveAsFlow()

    private val initialLoadedTabs = mutableSetOf<Int>()
    // Track active jobs untuk prevent duplicate flows
    private val activeJobs = mutableMapOf<Int, Job>()


    fun onActions(actions: ListTagihanActions) {
        when (actions) {
            is ListTagihanActions.NavigateToDetailTagihan -> navigateToDetailTagihan(actions.idTagihan)
            is ListTagihanActions.ChooseTab -> {
                if (actions.tabIndex != _state.value.selectedTab) {
                    updateSelectedTab(actions.tabIndex)

                    if (actions.tabIndex !in initialLoadedTabs){
                        loadTagihanData(getStatusFromTab(actions.tabIndex), actions.tabIndex, isInitialLoad = true)
                    }
                }
            }
            ListTagihanActions.TryAgain -> tryAgain()
        }
    }

    private fun navigateToDetailTagihan(idTagihan: String) {
        viewModelScope.launch {
            _events.send(ListTagihanEvent.NavigateToDetailTagihan(idTagihan))
        }
    }

    private fun updateSelectedTab(tabIndex: Int) {
        _state.update { it.copy(selectedTab = tabIndex) }
    }

    private fun getStatusFromTab(tabIndex: Int): TypeTagihan {
        return when(tabIndex){
            0 -> TypeTagihan.UNPAID
            1 -> TypeTagihan.WAITING_VERIFICATION
            2 -> TypeTagihan.PAID_OFF
            else -> TypeTagihan.UNPAID
        }
    }

    private fun tryAgain() {
        val currentTab = _state.value.selectedTab
        loadTagihanData(getStatusFromTab(currentTab), currentTab, true)
    }

    private fun getTabStateForIndex(tabIndex: Int): TabTagihanUi {
        return when(tabIndex) {
            0 -> _state.value.listTagihanBelumLunasUi
            1 -> _state.value.listTagihanMenungguVerifikasiUi
            2 -> _state.value.listTagihanLunasUi
            else -> TabTagihanUi()
        }
    }

    private fun updateTabState(tabIndex: Int, newState: TabTagihanUi) {
        _state.update { currentState ->
            when(tabIndex) {
                0 -> currentState.copy(listTagihanBelumLunasUi = newState)
                1 -> currentState.copy(listTagihanMenungguVerifikasiUi = newState)
                2 -> currentState.copy(listTagihanLunasUi = newState)
                else -> currentState
            }
        }
    }

    private fun loadTagihanData(status: TypeTagihan, tabIndex: Int, isInitialLoad: Boolean = false) {
        // Cancel previous job untuk tab ini
        activeJobs[tabIndex]?.cancel()

        val job = viewModelScope.launch {
            // ✅ Show loading HANYA untuk initial load
            if (isInitialLoad) {
                updateTabState(tabIndex, getTabStateForIndex(tabIndex).copy(isLoading = true, loadError = null))
            }

            getListTagihanUseCase(typeTagihan = status).collect { result ->
                when(result){
                    is Result.Error -> {
                        updateTabState(
                            tabIndex = tabIndex,
                            newState = getTabStateForIndex(tabIndex).copy(
                                isLoading = false,
                                loadError = result.error.message,
                                listTagihan = emptyList()
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
                                listTagihan = result.data
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
}