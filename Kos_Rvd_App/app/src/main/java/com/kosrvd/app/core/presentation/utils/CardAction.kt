package com.kosrvd.app.core.presentation.utils

sealed interface CardAction {

    fun onClick()
    data object None : CardAction {
        override fun onClick(){}
    }

    data class NavigationIconSide(val onClick: () -> Unit) : CardAction {
        override fun onClick() {
            onClick.invoke()
        }
    }

    data class NavigationIconFooter(val onClick: () -> Unit) : CardAction {
        override fun onClick() {
            onClick.invoke()
        }
    }

    data class Delete(val onDelete: () -> Unit) : CardAction {
        override fun onClick() {
            onDelete.invoke()
        }
    }
}