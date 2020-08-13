package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action

class AttackFirst: PassiveAction() {
    override fun copy(): Action {
        return AttackFirst()
    }
}