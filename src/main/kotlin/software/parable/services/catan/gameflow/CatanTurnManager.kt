package software.parable.services.catan.gameflow

import gameDataObjects.types.CatanColor
import software.parable.services.catan.gameflow.model.CatanBoardTurnState
import software.parable.services.catan.gameflow.model.CatanTurnStatus

object CatanTurnManager {

    private val defaultState = CatanBoardTurnState(
        CatanTurnStatus.INITIAL_PLACEMENT_FIRST,
        CatanColor.UNASSIGNED,
        0, 0, 0)
    private val playerOrdering = mutableSetOf<CatanColor>()
    private var currentPlayerIndex = 0

    var turnState: CatanBoardTurnState = defaultState

    fun startGame(playerOrdering: Set<CatanColor>) {
        this.resetTurnState(playerOrdering)
    }

    // TODO: Consider adding check whether player placed 2 road and 1 settlement during initial piece placement
    fun endTurn() {
        this.turnState.playerTurn = nextPlayer()
        this.turnState.status = evaluateStatus()
    }

    fun incrementRoad(times: Int = 1) {
        this.turnState.roadsPlaced += times
    }

    fun incrementSettlement(times: Int = 1) {
        this.turnState.settlementsPlaced += times
    }

    fun incrementDiceRollsSoFar(times: Int = 1) {
        this.turnState.diceRollsSoFar += times
    }

    private fun resetTurnState(playerOrdering: Set<CatanColor>) {
        this.turnState = defaultState
        this.playerOrdering.clear()
        this.playerOrdering.addAll(playerOrdering)
        this.currentPlayerIndex = 0
        this.turnState.playerTurn = this.playerOrdering.first()
    }

    // Need to handle initial player piece placement
    private fun nextPlayer(): CatanColor {
        this.currentPlayerIndex++
        val reverseSection = ((this.playerOrdering.size)until this.playerOrdering.size*2).contains(this.currentPlayerIndex)

        if (reverseSection) {
            // This is added to include the unique ordering of player piece
            // placements for the first 2 turns per player
            val index = nextPlayerIndexReverseSection()
            return this.playerOrdering.elementAt(index)
        }
        return this.playerOrdering.elementAt(((this.currentPlayerIndex) % this.playerOrdering.size))
    }

    private fun nextPlayerIndexReverseSection(): Int {
        val numOfPlayers = this.playerOrdering.size
        val subtractor = 1 + ((this.currentPlayerIndex - numOfPlayers)*2)
        return this.currentPlayerIndex - subtractor
    }

    // Need to handle victory conditions
    private fun evaluateStatus(): CatanTurnStatus {
        return if (this.currentPlayerIndex >= playerOrdering.size)
            CatanTurnStatus.INITIAL_PLACEMENT_FIRST
        else if (this.currentPlayerIndex < playerOrdering.size && this.currentPlayerIndex > playerOrdering.size * 2)
            CatanTurnStatus.INITIAL_PLACEMENT_SECOND
        else CatanTurnStatus.PLAYER_TURN_IN_PROGRESS
    }
}