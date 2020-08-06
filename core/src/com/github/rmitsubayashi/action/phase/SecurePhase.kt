package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.GameState
import com.github.rmitsubayashi.game.TicTacToeDamageCalculator
import com.github.rmitsubayashi.game.TicTacToeJudge

class SecurePhase(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.ENTER_SECURE_PHASE) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val newEvents = mutableListOf<Event>()
        //secure pieces that are currently on board
        val playerPieces = game.board.filter { it?.player?.id == player.id }
        for (p in playerPieces) {
            if (p != null && !p.isDead()){
                game.board.secure(p)
                newEvents.add(Event(EventType.pieceSecured, p, null))
            }
        }
        //check for tic tac toes
        val ticTacToes = TicTacToeJudge.listTicTacToeIndexes(game.board, player)
        player.score += ticTacToes.size
        for (ticTacToe in ticTacToes) {
            newEvents.add(Event(EventType.scored, player, null, mapOf(Pair(EventDataKey.SQUARE, ticTacToe))))
        }
        val gameProgress = game.gameJudge.checkWinner()
        if ((gameProgress as? GameState.Winner)?.winner == player) {
            newEvents.add(Event(EventType.playerWins, player, null))
            newEvents.add(
                    Event(EventType.shouldAnimate, null, null)
            )
            return newEvents
        }
        //tic tac toe pieces get damaged
        val damage = TicTacToeDamageCalculator.getDamage(game.gameProgressManager.turn)
        val uniquePieces = mutableSetOf<Piece>()
        for (ticTacToe in ticTacToes) {
            for (pieceIndex in ticTacToe) {
                val boardPiece = game.board[pieceIndex]
                if (boardPiece != null){
                    uniquePieces.add(boardPiece)
                }
            }
        }
        for (toDamagePiece in uniquePieces) {
            toDamagePiece.currHP -= damage
        }
        val damagedEvents = uniquePieces.map {
            Event(EventType.pieceDamaged, it, null)
        }
        newEvents.addAll(damagedEvents)
        newEvents.add(
                Event(EventType.ENTER_MONEY_DISTRIBUTION_PHASE, player, null)
        )
        return newEvents
    }

    override fun copy(): Action {
        return SecurePhase(eventActor as Player)
    }
}