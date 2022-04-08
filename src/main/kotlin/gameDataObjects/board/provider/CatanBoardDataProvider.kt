package gameDataObjects.board.provider

import gameDataObjects.model.CatanHexagonPieceModel
import gameDataObjects.types.*

interface CatanBoardDataProvider {
    fun getBoardGamePieceLocations(): MutableMap<CatanCoordinate, CatanGamePiece?>
    fun getBoardRoadPieceLocations(): MutableMap<CatanRoadCoordinates, CatanColor?>
    fun getResourceTileInfo(): MutableMap<CatanCoordinate, CatanHexagonPieceModel>
    fun getPlayerResourceCards(): HashMap<CatanColor, MutableList<CatanResource>>
    fun getBoardPlayers(): Set<CatanColor>
}