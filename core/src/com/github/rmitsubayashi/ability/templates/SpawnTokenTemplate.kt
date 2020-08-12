package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.ability.TargetWordingHelper
import com.github.rmitsubayashi.ability.TimingWordingHelper
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.SpawnToken
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Stats

class SpawnTokenTemplate: AbilityTemplate {
    override fun getMatcher(): Regex {
        val regex = "(${TimingWordingHelper.whenPlayed})spawn a ([0-9]+)\\/([0-9]+) ([a-zA-Z ]+) in (${TargetWordingHelper.randomSquare})"
        return Regex(regex)
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): Action? {
        val matcher = getMatcher()
        val matches = matcher.find(abilityDescription) ?: return null
        val timingString = matches.groups[1]?.value ?: return null
        val timing = TimingWordingHelper.wordToEventType(timingString) ?: return null
        val attack = matches.groups[2]?.value?.toInt() ?: return null
        val hp = matches.groups[3]?.value?.toInt() ?: return null
        val name = matches.groups[4]?.value ?: return null
        val targetString = matches.groups[5]?.value ?: return null
        val target = TargetWordingHelper.wordToEventActor(targetString) ?: return null
        val token = Piece(name, piece.race, Stats(hp, attack), "", piece.attackRange, 1, emptyList(), piece.player)
        token.toToken()
        return SpawnToken(piece, token, timing, target)
    }
}