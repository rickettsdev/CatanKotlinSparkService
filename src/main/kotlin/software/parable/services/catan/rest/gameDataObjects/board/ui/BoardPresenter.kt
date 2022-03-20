package gameDataObjects.board.ui

import gameDataObjects.board.theme.CatanThemedPrinter
import gameDataObjects.constants.BoardSetupConstants
import gameDataObjects.model.CatanHexagonPieceModel
import gameDataObjects.types.*
import gameDataObjects.viewmodel.CatanHexCornerNodeViewModel
import kotlin.math.floor

class BoardPresenter(private val themePrinter: CatanThemedPrinter = CatanThemedPrinter()) {
    fun generateBoard(
        gamePieceLocations: MutableMap<CatanCoordinate, CatanGamePiece?>,
        roadPieceLocations: MutableMap<CatanRoadCoordinates, CatanColor?>,
        hexCoordinateNodeViewModelMap: MutableMap<CatanCoordinate, CatanHexCornerNodeViewModel>,
        resourceMap: MutableMap<CatanCoordinate, CatanHexagonPieceModel>,
        robberCoordinate: CatanCoordinate
    ): String {

        var boardString = ""

        for( currentBoardHeight in 0 until BoardSetupConstants.BOARD_HEIGHT_IN_POINTS) {
            val coordinates = gamePieceLocations.keys.filter { it.y == currentBoardHeight }
            val pointTabPadding = when(currentBoardHeight) {
                0, 11 -> "\t\t\t\t"
                1, 2, 9, 10 -> "\t\t\t"
                3, 4, 8, 7 -> "\t\t"
                else -> "\t"
            }
            val barTabPadding = when (currentBoardHeight) {
                0, 10 -> "\t\t\t"
                1, 2 -> "\t\t"
                6 -> ""
                else -> "\t"
            }
            var pointString = pointTabPadding
            for (coordinate in coordinates.sortedBy { it.x }) {
                val color: CatanColor = hexCoordinateNodeViewModelMap[coordinate]?.gamePiece?.color ?: CatanColor.UNASSIGNED
                val hexagonPieceCoordinate = CatanCoordinate(coordinate.x, coordinate.y / 2)
                val secondHexagonPieceCoordinate = CatanCoordinate(coordinate.x, floor(coordinate.y.toDouble()/2.5).toInt())
                val hexagonFillerString = if (coordinate.y % 2 == 1 && robberCoordinate.equals(inferRobberCoordinates(coordinate))) themePrinter.printTheme("ROB", CatanColor.UNASSIGNED)
                else if (coordinate.y % 2 == 1 && coordinate.y != 11 && coordinate.x < BoardSetupConstants.SUBCOORDINATE_ROW_MAX_LENGTHS[coordinate.y]-1)
                    resourceMap[hexagonPieceCoordinate]?.resource.toString().substring(0..2)
                else if (coordinate.y % 2 == 0 && coordinate.y != 0 && coordinate.x < BoardSetupConstants.SUBCOORDINATE_ROW_MAX_LENGTHS[coordinate.y]-1 && resourceMap[secondHexagonPieceCoordinate]?.diceRoll != 7)
                    resourceMap[secondHexagonPieceCoordinate]?.diceRoll
                else ""
                pointString += "${generateSubCoordinateSymbol(gamePieceLocations, coordinate, color)}\t$hexagonFillerString\t"
            }
            boardString += pointString
            println(pointString)
            // Next, the roads
            // TODO: This string has a hacky else if statement.
            var roadString = if (currentBoardHeight % 2 == 1) "$pointTabPadding" else if(currentBoardHeight == 10) "\t\t" else "$barTabPadding"
            for (coordinate in coordinates.sortedBy { it.x }) {
                if (coordinate.y != BoardSetupConstants.BOARD_HEIGHT_IN_POINTS-1) {
                    if (currentBoardHeight % 2 == 1) {
                        val color = roadPieceLocations[CatanRoadCoordinates(listOf(coordinate, CatanCoordinate(coordinate.x, coordinate.y+1)))] ?: CatanColor.UNASSIGNED
                        roadString += "${themePrinter.printTheme("|", color)}\t\t"
                    }
                    else {
                        // diagonal roads
                        val roadsBelow = coordinatesBelow(coordinate)
                        val leftRoad = if (roadsBelow.first != null) CatanRoadCoordinates(listOf(coordinate, roadsBelow.first!!)) else null
                        val rightRoad = if (roadsBelow.second != null) CatanRoadCoordinates(listOf(coordinate, roadsBelow.second!!)) else null

                        val leftRoadColor = roadPieceLocations[leftRoad] ?: CatanColor.UNASSIGNED
                        val rightRoadColor = roadPieceLocations[rightRoad] ?: CatanColor.UNASSIGNED

                        val leftRoadCharacter = if (leftRoad != null) themePrinter.printTheme("/", leftRoadColor) else " "
                        val rightRoadCharacter = if (rightRoad != null) themePrinter.printTheme("\\", rightRoadColor) else " "

                        roadString += " $leftRoadCharacter\t   $rightRoadCharacter"
                    }
                }
            }
            boardString += roadString
            println(roadString)
        }
        return ""
    }
    // Only works for subcoordinates where y % 2 == 0
    private fun coordinatesBelow(coordinate: CatanCoordinate): Pair<CatanCoordinate?, CatanCoordinate?> {
        val leftCoordinate = if (coordinate.y <= 4) CatanCoordinate(coordinate.x, coordinate.y + 1)
        else if (coordinate.y >= 6 && coordinate.x != 0 && coordinate.y != 11) CatanCoordinate(coordinate.x - 1, coordinate.y + 1)
        else null

        val rightCoordinate = if (coordinate.y <= 4) CatanCoordinate(coordinate.x + 1, coordinate.y + 1)
        else if (coordinate.y >= 6 && coordinate.y != 11 && coordinate.x != BoardSetupConstants.SUBCOORDINATE_ROW_MAX_LENGTHS[coordinate.y] - 1)
            CatanCoordinate(coordinate.x, coordinate.y + 1)
        else null

        return Pair(leftCoordinate, rightCoordinate)
    }

    //Checks gamepiece locations to check whether a city or settlement is build on it.
    private fun generateSubCoordinateSymbol(gamePieceLocations: MutableMap<CatanCoordinate, CatanGamePiece?>, coordinate: CatanCoordinate, color: CatanColor): String {
        if (color == CatanColor.UNASSIGNED)
            return "*"

        return when(gamePieceLocations[coordinate]?.pieceType) {
            CatanPiece.CITY -> themePrinter.printTheme("C", color)
            CatanPiece.SETTLEMENT -> themePrinter.printTheme("S", color)
            else -> themePrinter.printTheme("*", CatanColor.ERROR)
        }
    }
    // given that the subcoordinate is on the same row as the resource
    // name it will be overriding
    private fun inferRobberCoordinates(subcoordinate: CatanCoordinate): CatanCoordinate {
        return CatanCoordinate(subcoordinate.x, subcoordinate.y / 2)
    }
}