package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.ability.TargetWordingHelper
import com.github.rmitsubayashi.ability.TimingWordingHelper
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.DealEffectDamage
import com.github.rmitsubayashi.entity.Piece

class DealDamageTemplate: AbilityTemplate {
    override fun getMatcher(): Regex {
        val regex = "(${TimingWordingHelper.whenPlayed}|${TimingWordingHelper.whenDestroyed})"+
                "deal ([0-9]+) damage to "+
                "(${TargetWordingHelper.opponentPiece}|${TargetWordingHelper.randomOpponentPiece}|${TargetWordingHelper.allOpponentPieces}|${TargetWordingHelper.adjacentPieces})"
        return Regex(regex)
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): Action? {
        val matcher = getMatcher()
        val matches = matcher.find(abilityDescription) ?: return null
        val timingString = matches.groups[1]?.value ?: return null
        val timing = TimingWordingHelper.wordToEventType(timingString) ?: return null
        val amount = matches.groups[2]?.value?.toInt() ?: return null
        val targetString = matches.groups[3]?.value ?: return null
        val target = TargetWordingHelper.wordToEventActor(targetString) ?: return null
        return DealEffectDamage(piece, timing, target, amount)
    }
}