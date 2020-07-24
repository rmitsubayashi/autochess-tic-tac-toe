package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.action.piece.Damaged
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.*

class SetupPhase(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.enterSetupPhase) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val player = event.actor as Player
        val newEvents = mutableListOf<Event>()
        //secure pieces that are currently on board
        val playerPieces = game.board.filter { it?.player?.id == player.id }
        for (p in playerPieces) {
            if (p != null){
                if (!game.board.isSecured(p) && !p.isDead()) {
                    game.board.secure(p)
                    newEvents.add(Event(EventType.pieceSecured, p, null))
                }
            }
        }
        //check for tic tac toes
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
        newEvents.addAll(damagedEvents)

        //add money for that turn
        player.money = player.money + MoneyCalculator.calculateStartOfTurnMoney(player)
        newEvents.add(
                Event(EventType.moneyChanged, player, null)
        )
        //the player gets new pieces from the pool.
        //note that this is different from a regular roll because no money is spent
        game.piecePool.refresh(player)
        newEvents.add(
                Event(EventType.reroll, player, null,
                        mapOf(Pair(EventDataKey.DONE, true), Pair(EventDataKey.IS_USER_EVENT, false)))
        )
        newEvents.add(
                Event(EventType.shouldAnimate, null, null)
        )
        newEvents.add(
                // this marks the end of the automatic actions regarding the set up phase.
                // the user can still roll for pieces and place them on board.
                // when the user is done with all manual setup, enterBattlePhase is triggered
                Event(EventType.enterSetupPhase, player, null, mapOf(Pair(EventDataKey.DONE, true)))
        )
        return newEvents
    }

    override fun copy(): Action {
        return SetupPhase(eventActor)
    }
}