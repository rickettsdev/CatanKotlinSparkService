package gameDataObjects.board

import gameDataObjects.types.*

interface CatanBoardRequirements {
    fun numberRolled(number: Int): HashMap<CatanPlayer, MutableList<CatanResource>>
    fun moveRobber(coordinate: CatanCoordinate)
    fun placeSettlement(subCoordinate: CatanCoordinate, gamePiece: CatanGamePiece)
    fun placeRoad(roadCoordinates: CatanRoadCoordinates, color: CatanColor)
    fun getVictoryPoints(): HashMap<CatanColor, Int>
    fun playerHasPortAccess(player: CatanPlayer, portResource: CatanPortType)
    fun printBoard()
}