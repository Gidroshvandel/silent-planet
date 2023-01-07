package com.silentgames.core.utils

fun <T1, T2, T3> notNull(t1: T1?, t2: T2?, body: (T1, T2) -> T3): T3? =
    if (t1 != null && t2 != null) {
        body(t1, t2)
    } else null

fun <T1, T2, T3, T4> notNull(t1: T1?, t2: T2?, t3: T3?, body: (T1, T2, T3) -> T4): T4? =
    if (t1 != null && t2 != null && t3 != null) {
        body(t1, t2, t3)
    } else null

fun <T1, T2, T3, T4, T5> notNull(
    t1: T1?,
    t2: T2?,
    t3: T3?,
    t4: T4?,
    body: (T1, T2, T3, T4) -> T5
): T5? =
    if (t1 != null && t2 != null && t3 != null && t4 != null) {
        body(t1, t2, t3, t4)
    } else null

fun <T1, T2, T3, T4, T5, T6> notNull(
    t1: T1?,
    t2: T2?,
    t3: T3?,
    t4: T4?,
    t5: T5?,
    body: (T1, T2, T3, T4, T5) -> T6
): T6? =
    if (t1 != null && t2 != null && t3 != null && t4 != null && t5 != null) {
        body(t1, t2, t3, t4, t5)
    } else null
