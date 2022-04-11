package software.parable.services.catan.gameflow.model

import gameDataObjects.types.CatanColor

data class CatanBoardTurnState(
    var status: CatanTurnStatus,
    var playerTurn: CatanColor,
    var roadsPlaced: Int,
    var settlementsPlaced: Int,
    var diceRollsSoFar: Int
)