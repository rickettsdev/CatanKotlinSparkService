package software.parable.services.catan.gameflow

import gameDataObjects.types.CatanColor

object CatanTurnManager {
    private data class CatanBoardTurnState(
        var status: CatanTurnStatus,
        var playerTurn: CatanColor,
        var roadsPlaced: Int,
        var settlementsPlaced: Int,
        var diceRollsSoFar: Int
        )
    private enum class CatanTurnStatus {
        INITIAL_PLACEMENT_FIRST,
        INITIAL_PLACEMENT_SECOND,
        PLACE_ROBBER,
        OVER_7_DISCARD,
        PLAYER_VICTORY
    }

    private val defaultState = CatanBoardTurnState(CatanTurnStatus.INITIAL_PLACEMENT_FIRST, CatanColor.UNASSIGNED, 0, 0, 0)
    private val playerOrdering = mutableSetOf<CatanColor>()

    private var turnState: CatanBoardTurnState = defaultState
    fun startGame(playerOrdering: Set<CatanColor>) {
        this.resetTurnState(playerOrdering)
    }

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
        this.turnState.playerTurn = this.playerOrdering.first()
    }

    // Need to handle initial player piece placement
    private fun nextPlayer(): CatanColor {
        return CatanColor.RED
    }

    // Need to handle initial player piece placement
    private fun evaluateStatus(): CatanTurnStatus {
        return CatanTurnStatus.INITIAL_PLACEMENT_FIRST
    }
}