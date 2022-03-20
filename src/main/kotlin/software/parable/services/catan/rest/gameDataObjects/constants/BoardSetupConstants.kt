package gameDataObjects.constants

import gameDataObjects.types.CatanCoordinate
import gameDataObjects.types.CatanRoadCoordinates

class BoardSetupConstants {
    companion object {
        val VALID_COORDINATES: List<CatanCoordinate> = listOf(
            CatanCoordinate(0, 0),
            CatanCoordinate(1, 0),
            CatanCoordinate(2, 0),
            CatanCoordinate(0, 1),
            CatanCoordinate(1, 1),
            CatanCoordinate(2, 1),
            CatanCoordinate(3, 1),
            CatanCoordinate(0, 2),
            CatanCoordinate(1, 2),
            CatanCoordinate(2, 2),
            CatanCoordinate(3, 2),
            CatanCoordinate(4, 2),
            CatanCoordinate(0, 3),
            CatanCoordinate(1, 3),
            CatanCoordinate(2, 3),
            CatanCoordinate(3, 3),
            CatanCoordinate(0, 4),
            CatanCoordinate(1, 4),
            CatanCoordinate(2, 4)
        )
        val BOARD_HEIGHT_IN_POINTS = 12
        val LIST_UPPER_BOUND = 6
        val LIST_LOWER_BOUND = -1
        val ROW_MAX_LENGTHS = listOf(3, 4, 5, 4, 3)
        val SUBCOORDINATE_ROW_MAX_LENGTHS = listOf(3, 4, 4, 5, 5, 6, 6, 5, 5, 4, 4, 3)
        val EMPTY_ROAD = CatanRoadCoordinates(listOf(CatanCoordinate(-1, -1), CatanCoordinate(-1, -1)))
    }
}