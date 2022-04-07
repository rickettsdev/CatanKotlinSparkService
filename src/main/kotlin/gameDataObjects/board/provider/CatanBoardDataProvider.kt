package gameDataObjects.board.provider

import gameDataObjects.model.CatanHexagonPieceModel
import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanCoordinate
import gameDataObjects.types.CatanGamePiece
import gameDataObjects.types.CatanRoadCoordinates

interface CatanBoardDataProvider {
    fun getBoardGamePieceLocations(): MutableMap<CatanCoordinate, CatanGamePiece?>
    fun getBoardRoadPieceLocations(): MutableMap<CatanRoadCoordinates, CatanColor?>
    fun getResourceTileInfo(): MutableMap<CatanCoordinate, CatanHexagonPieceModel>
    fun getBoardPlayers(): Set<CatanColor>
}