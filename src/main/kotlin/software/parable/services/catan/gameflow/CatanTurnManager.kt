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
            // This is added to include the unique ordering of player piece placements for the first 8 turns.
            // TODO: Fix: Only works for 4 players.
            val index = when (this.currentPlayerIndex) {
                4 -> 3
                5 -> 2
                6 -> 1
                7 -> 0
                else -> throw Exception("Invalid")
            }
            return this.playerOrdering.elementAt(index)
        }
        return this.playerOrdering.elementAt(((this.currentPlayerIndex) % this.playerOrdering.size))
    }

    // Need to handle victory conditions
    private fun evaluateStatus(): CatanTurnStatus {
        return when(this.currentPlayerIndex) {
            0, 1, 2, 3 -> CatanTurnStatus.INITIAL_PLACEMENT_FIRST
            4, 5, 6, 7 -> CatanTurnStatus.INITIAL_PLACEMENT_SECOND
            else -> CatanTurnStatus.PLAYER_TURN_IN_PROGRESS
        }
    }
}