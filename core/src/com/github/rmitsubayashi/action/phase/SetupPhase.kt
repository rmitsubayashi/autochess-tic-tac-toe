package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventActor
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.action.piece.Damaged
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.*

class SetupPhase(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return event.type == EventType.enterSetupPhase && event.actor == eventActor
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val player = event.actor as Player
        //secure pieces that are currently on board
        val playerPieces = game.board.filter { it?.player?.id == player.id }
        for (p in playerPieces) {
            if (p != null){
                game.board.secure(p)
            }
        }
        //check for winner
        val ticTacToes = TicTacToeJudge.listTicTacToeIndexes(game.board, player)
        player.score += ticTacToes.size
        val gameProgress = game.gameJudge.checkWinner()
        if ((gameProgress as? GameState.Winner)?.winner == player) {
            return listOf(Event(EventType.playerWins, player, null))
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

        //add money for that turn
        player.money = player.money + MoneyCalculator.calculateStartOfTurnMoney(player)
        //the player gets new pieces from the pool
        game.piecePool.refresh(player)
        return damagedEvents
    }

    override fun copy(): Action {
        return SetupPhase(eventActor)
    }
}