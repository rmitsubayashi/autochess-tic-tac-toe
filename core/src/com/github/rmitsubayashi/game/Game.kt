package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.ability.AbilityList
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.ActionObservable
import com.github.rmitsubayashi.action.Animate
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.phase.*
import com.github.rmitsubayashi.action.piece.Attack
import com.github.rmitsubayashi.action.piece.Damaged
import com.github.rmitsubayashi.action.piece.DeclareAttack
import com.github.rmitsubayashi.action.player.BuyPiece
import com.github.rmitsubayashi.action.player.PlacePiece
import com.github.rmitsubayashi.action.player.Reroll
import com.github.rmitsubayashi.action.player.SellPiece
import com.github.rmitsubayashi.ai.AISetupPhase
import com.github.rmitsubayashi.ai.AIUserInput
import com.github.rmitsubayashi.ai.CrudeAISetupPhase
import com.github.rmitsubayashi.ai.CrudeAIUserInput
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.setup.PieceFileReader
import com.github.rmitsubayashi.setup.ShopConfigReader
import com.github.rmitsubayashi.setup.PlayerLevelCostReader

class Game(
        private val pieceFileReader: PieceFileReader,
        private val shopConfigReader: ShopConfigReader,
        private val playerLevelCostReader: PlayerLevelCostReader
) {
    val actionObservable = ActionObservable(this)
    val board = Board()
    val player1 = Player(1)
    val player2 = Player(2)
    private lateinit var player1Shop: Shop
    private lateinit var player2Shop: Shop
    val gameJudge = GameJudge(5, player1, player2)
    val gameProgressManager = GameProgressManager(this)
    val animationQueue = AnimationQueue()
    val userInputManager = UserInputManager(this)
    val raceSelector = RaceSelector()
    lateinit var playerLevelManager: PlayerLevelManager

    init {
        setupShop()
        setupPlayerLevelManager()
        loadInitialActions()
    }

    private fun setupShop() {
        val pieces = pieceFileReader.read(AbilityList())
        val configs = shopConfigReader.read()
        player1Shop = Shop(pieces, configs, player1)
        player2Shop = Shop(pieces, configs, player2)
    }

    private fun setupPlayerLevelManager() {
        val levelUpCosts = playerLevelCostReader.read()
        playerLevelManager = PlayerLevelManager(levelUpCosts)
    }

    private fun loadInitialActions() {
        val actions = mutableListOf<Action>()
        actions.add(SecurePhase(player1))
        actions.add(SecurePhase(player2))
        actions.add(MoneyDistributionPhase(player1))
        actions.add(MoneyDistributionPhase(player2))
        actions.add(DeckBuildingPhase(player1))
        actions.add(DeckBuildingPhase(player2))
        actions.add(SetupPhase(player1))
        actions.add(SetupPhase(player2))
        actions.add(BattlePhase(player1))
        actions.add(BattlePhase(player2))
        actions.add(Reroll(player1))
        actions.add(Reroll(player2))
        actions.add(BuyPiece(player1))
        actions.add(BuyPiece(player2))
        actions.add(PlacePiece(player1))
        actions.add(PlacePiece(player2))
        actions.add(SellPiece(player1))
        actions.add(AISetupPhase(player2, CrudeAISetupPhase()))
        actions.add(AIUserInput(player2, CrudeAIUserInput()))
        actions.add(Damaged())
        actions.add(DeclareAttack())
        actions.add(Attack())
        actions.add(Animate())

        actionObservable.subscribeActions(actions)
    }

    fun notifyEvent(event: Event) {
        actionObservable.notifyAllActions(event)
    }

    fun waitForUserInput(triggeredAction: Action, associatedEvent: Event, possibleTargets: List<Piece>) {
        userInputManager.waitForUserInput(triggeredAction, associatedEvent, possibleTargets)
    }

    // for when you know the actor but the acted upon is an unspecified target
    fun getOpposingPlayer(player: Player?): Player? {
        return when (player) {
            player1 -> player2
            player2 -> player1
            else -> null
        }
    }

    fun getShop(player: Player): Shop? {
        return when (player) {
            player1 -> player1Shop
            player2 -> player2Shop
            else -> null
        }
    }
}