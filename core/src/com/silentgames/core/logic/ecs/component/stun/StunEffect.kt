package com.silentgames.core.logic.ecs.component.stun

import com.silentgames.core.logic.ecs.component.ComponentEquals

class StunEffect(private var _stunTurnsLeft: Int, val totalStunTurns: Int) : ComponentEquals() {

    constructor(stunComponent: StunComponent) : this(stunComponent.stunTurns, stunComponent.stunTurns)

    val stunTurnsLeft get() = _stunTurnsLeft

    fun skipStunTurn() {
        _stunTurnsLeft--
        if (_stunTurnsLeft < 0) {
            _stunTurnsLeft = 0
        }
    }

    fun canMove() = _stunTurnsLeft == 0
}