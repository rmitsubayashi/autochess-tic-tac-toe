package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.ability.TargetWordingHelper
import com.github.rmitsubayashi.ability.TimingWordingHelper
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.DealEffectDamage
import com.github.rmitsubayashi.entity.Piece

class DealDamage: AbilityTemplate {
    override fun getMatcher(): Regex {
        val regex = "(${TimingWordingHelper.whenPlayed}|${TimingWordingHelper.whenDestroyed})"+
                "deal ([0-9]+) damage to "+
                "(${TargetWordingHelper.opponentPiece}|${TargetWordingHelper.randomOpponentPiece}|${TargetWordingHelper.allOpponentPieces})"
        return Regex(regex)
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): List<Action> {
        val matcher = getMatcher()
        val matches = matcher.find(abilityDescription) ?: return emptyList()
        val timingString = matches.groups[1].toString()
        val timing = TimingWordingHelper.wordToEventType(timingString) ?: return emptyList()
        val amount = matches.groups[2].toString().toInt()
        val targetString = matches.groups[3].toString()
        val target = TargetWordingHelper.wordToEventActor(targetString) ?: return emptyList()
        return listOf(
                DealEffectDamage(piece, timing, target, amount)
        )
    }
}