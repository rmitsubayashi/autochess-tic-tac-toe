package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.ability.TargetWordingHelper
import com.github.rmitsubayashi.ability.TimingWordingHelper
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.Move
import com.github.rmitsubayashi.entity.Piece

class MoveTemplate: AbilityTemplate {
    override fun getMatcher(): Regex {
        val regex = "(${TimingWordingHelper.whenPlayed})move (${TargetWordingHelper.allPieces}|${TargetWordingHelper.opponentPiece}) to (${TargetWordingHelper.randomSquare})"
        return Regex(regex)
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): Action? {
        val matcher = getMatcher()
        val matches = matcher.find(abilityDescription) ?: return null
        val timingString = matches.groups[1]?.value ?: return null
        val timing = TimingWordingHelper.wordToEventType(timingString) ?: return null
        val targetString = matches.groups[2]?.value ?: return null
        val target = TargetWordingHelper.wordToEventActor(targetString) ?: return null
        val squareTargetString = matches.groups[3]?.value ?: return null
        val squareTarget = TargetWordingHelper.wordToEventActor(squareTargetString) ?: return null
        return Move(piece, timing, target, squareTarget)

    }
}