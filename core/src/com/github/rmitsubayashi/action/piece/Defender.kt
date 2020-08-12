package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action


class Defender: PassiveAction() {
    override fun copy(): Action {
        return Defender()
    }
}