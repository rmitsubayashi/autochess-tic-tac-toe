package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action

class IgnoreDefender: PassiveAction() {
    override fun copy(): Action {
        return IgnoreDefender()
    }
}