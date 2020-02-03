package com.silentgames.core.logic

object CoreLogger {

    var onMessageLogged: ((tag: String, message: String) -> Unit)? = null

    fun logDebug(tag: String, message: String) {
        onMessageLogged?.invoke(tag, message)
    }

}