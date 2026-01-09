package com.innovation.readify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class BaseViewModel<UI_State, UI_EFFECT, UI_EVENT>(
  private val initialState: UI_State,
) : ViewModel() {

  protected val state: UI_State
    get() = _uiState.value

  private var _uiState: MutableStateFlow<UI_State> = MutableStateFlow(initialState)
  val uiState = _uiState.asStateFlow()

  private val _uiEffect by lazy { Channel<UI_EFFECT>(Channel.UNLIMITED) }
  val uiEffect: Flow<UI_EFFECT> by lazy { _uiEffect.receiveAsFlow() }


  private val _uiEvent = Channel<UI_EVENT>(Channel.UNLIMITED)
  private val uiEvent: Flow<UI_EVENT> = _uiEvent.receiveAsFlow()


  init {
    processEvents()
  }

  protected open fun updateEffect(newEffect: UI_EFFECT) {
    viewModelScope.launch {
      _uiEffect.send(newEffect)
    }
  }


  protected open fun updateState(newState: UI_State.() -> UI_State) {
    _uiState.update { state.newState() }
  }


  // Emit a new event
  fun sendEvent(event: UI_EVENT) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }


  // Process events sequentially
  private fun processEvents() {
    viewModelScope.launch {
      uiEvent.collect { event ->
        handleEvent(event)
      }
    }
  }


  protected abstract fun handleEvent(event: UI_EVENT)


}