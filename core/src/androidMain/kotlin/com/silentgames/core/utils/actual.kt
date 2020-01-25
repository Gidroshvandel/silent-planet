package com.silentgames.core.utils

//actual fun platformName(): String {
//    return UIDevice.currentDevice.systemName() +
//            " " +
//            UIDevice.currentDevice.systemVersion
//}

actual fun format(string: String, vararg args: Any?): String {
    return String.format(string, *args)
}
