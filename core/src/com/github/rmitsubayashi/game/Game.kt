package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.ability.AbilityList
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.ActionObservable
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.phase.BattlePhase
import com.github.rmitsubayashi.action.player.BuyPiece
import com.github.rmitsubayashi.action.player.PlacePiece
import com.github.rmitsubayashi.action.player.Reroll
import com.github.rmitsubayashi.action.player.SellPiece
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.setup.PlayerLevelCostReader
import com.github.rmitsubayashi.setup.PieceFileReader
import com.github.rmitsubayashi.setup.PiecePoolConfigReader
import kotlin.random.Random

class Game(
        private val pieceFileReader: PieceFileReader,
        private val piecePoolConfigReader: PiecePoolConfigReader,
        private val playerLevelCostReader: PlayerLevelCostReader
) {
    val actionObservable = ActionObservable(this)
    val board = Board()
    val player1 = Player(1)
    val player2 = Player(2)
    val gameJudge = GameJudge(5, player1, player2)
    val gameProgressManager = GameProgressManager(this)
    lateinit var playerLevelManager: PlayerLevelManager
    lateinit var piecePool: PiecePool

    init {
        setupPiecePool()
        setupPlayerLevelManager()
        loadInitialActions()
    }

    private fun setupPiecePool() {
        val pieces = pieceFileReader.read(AbilityList())
        val configs = piecePoolConfigReader.read()
        piecePool = PiecePool(pieces, configs, Random.Default)
    }

    private fun setupPlayerLevelManager() {
        val levelUpCosts = playerLevelCostReader.read()
        playerLevelManager = PlayerLevelManager(levelUpCosts)
    }

    private fun loadInitialActions() {
        val actions = mutableListOf<Action>()
        actions.add(BattlePhase(player1))
        actions.add(BattlePhase(player2))
        actions.add(Reroll(player1))
        actions.add(BuyPiece(player1))
        actions.add(PlacePiece(player1))
        actions.add(SellPiece(player1))

        actionObservable.subscribeActions(actions)
    }

    fun notifyEvent(event: Event) {
        actionObservable.notifyAllActions(event)
    }

    fun waitForUserInput(triggeredAction: Action) {
        actionObservable.waitingForUserInput(triggeredAction)
        //TODO ui update here based on type of user input required
        // (for example board piece, hand piece, etc.)
    }

    // for when you know the actor but the acted upon is an unspecified target
    fun getOpposingPlayer(player: Player?): Player? {
        return when (player) {
            player1 -> player2
            player2 -> player1
            else -> null
        }
    }
}