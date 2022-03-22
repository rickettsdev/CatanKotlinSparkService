package gameDataObjects.board.manager

import gameDataObjects.board.provider.CatanBoardDataProvider
import gameDataObjects.types.*

class CatanGamePieceStateManager(private val boardDataProvider: CatanBoardDataProvider) {

    fun isValidRoadPlacement(roadCoordinates: CatanRoadCoordinates?, color: CatanColor, visitedRoads: MutableList<CatanRoadCoordinates> = mutableListOf()): Boolean {

        println("Checking Road Coordinate $roadCoordinates")

        if (roadCoordinates == null || visitedRoads.contains(roadCoordinates)) {
            return false
        }

        visitedRoads.add(roadCoordinates)

        val firstColor =
            if (roadCoordinates.roadCoordinates.first() != null) boardDataProvider.getBoardGamePieceLocations()[roadCoordinates.roadCoordinates.first()]?.color
            else null
        val secondColor =
            if(roadCoordinates.roadCoordinates.last() != null) boardDataProvider.getBoardGamePieceLocations()[roadCoordinates.roadCoordinates.last()]?.color
            else null

        val settlementPieces = boardDataProvider.getBoardGamePieceLocations().filter { it.value != null }.keys

        val firstCoordinateOtherTeamSettlement = settlementPieces.filter {
            it.x == roadCoordinates.roadCoordinates.first().x && it.y == roadCoordinates.roadCoordinates.first().y &&
                    boardDataProvider.getBoardGamePieceLocations()[it]?.color != color
        }.isNotEmpty()

        val secondCoordinateOtherTeamSettlement = settlementPieces.filter {
            it.x == roadCoordinates.roadCoordinates.last().x && it.y == roadCoordinates.roadCoordinates.last().y &&
                    boardDataProvider.getBoardGamePieceLocations()[it]?.color != color
        }.isNotEmpty()

        var connectedToSettlementPiece = settlementPieces.filter {
            (it == roadCoordinates.roadCoordinates.first() && color == firstColor) ||
                    (it == roadCoordinates.roadCoordinates.last() && color == secondColor)
        }.isNotEmpty()

        if (connectedToSettlementPiece) return true

        val connectedRoadsFirst = boardDataProvider.getBoardRoadPieceLocations().keys.filter {
            boardDataProvider.getBoardRoadPieceLocations()[it] == color && it.roadCoordinates.contains(roadCoordinates.roadCoordinates.first()) && it != roadCoordinates
        }
        val connectedRoadsLast = boardDataProvider.getBoardRoadPieceLocations().keys.filter {
            boardDataProvider.getBoardRoadPieceLocations()[it] == color && it.roadCoordinates.contains(roadCoordinates.roadCoordinates.last()) && it != roadCoordinates
        }

        val newConnectedRoads: MutableList<CatanRoadCoordinates> = if (!firstCoordinateOtherTeamSettlement) connectedRoadsFirst.toMutableList() else mutableListOf()

        if (!secondCoordinateOtherTeamSettlement)
            newConnectedRoads.addAll(connectedRoadsLast)

        if (newConnectedRoads.isEmpty()) return false

        var isValidRoadPlacement: Boolean = false

        for (road in newConnectedRoads) {
            isValidRoadPlacement = isValidRoadPlacement || isValidRoadPlacement(road, color, visitedRoads)
        }

        return isValidRoadPlacement
    }


    /*TODO: Check for:
    *
    * 1. Whether or not the piece to be placed is a city, and if a same colored settlement exists there
    * 2. If a settlement, make sure no other piece exists there
    * 3. Make sure all other settlements are at least 2 roads length away.
    *
    * */
    fun isValidSettlementLocation(newSettlementCoordinate: CatanCoordinate, newSettlementPiece: CatanGamePiece): Boolean {
        if (newSettlementPiece.pieceType == CatanPiece.CITY && boardDataProvider.getBoardGamePieceLocations()[newSettlementCoordinate]?.color == newSettlementPiece.color) {
            return true
        }

        val connectingRoads = boardDataProvider.getBoardRoadPieceLocations().filter {
            it.key.roadCoordinates.contains(newSettlementCoordinate)
        }.keys.map {
                roadCoordinate -> roadCoordinate.roadCoordinates.filterNot {
            it == newSettlementCoordinate
        }
        }.flatten()

        val villageTooClose: Boolean = connectingRoads.filter { boardDataProvider.getBoardGamePieceLocations()[it]?.pieceType != null }.isNotEmpty()

        return boardDataProvider.getBoardGamePieceLocations()[newSettlementCoordinate] == null && !villageTooClose
    }

    // Victory Point stuff. Probably should move to different file

    /**
     * @output: Map of color and their longest road coordinates list
     * */
    fun provideLongestRoad(): MutableMap<CatanColor, List<CatanRoadCoordinates>> {
        println("HERERERERE")
        var longestRoads = mutableMapOf<CatanColor, List<CatanRoadCoordinates>>()
        for (player in boardDataProvider.getBoardPlayers()) {
            println("Player: $player")
            val roadsOfPlayer: MutableSet<CatanRoadCoordinates> = boardDataProvider.getBoardRoadPieceLocations().filter { it.value == player}.keys.toMutableSet()
            println("Roads of player $roadsOfPlayer")

            var longestRoadCoordinates: MutableList<CatanRoadCoordinates> = mutableListOf()

            for (road in roadsOfPlayer) {
                println("Road Piece: $road")
                val playersRoadMaxLength = findLongestRoad(mutableListOf(road), roadsOfPlayer, player)

                if (playersRoadMaxLength.size > longestRoadCoordinates.size) {
                    longestRoadCoordinates = playersRoadMaxLength
                }
            }
            longestRoads[player] = longestRoadCoordinates
            println("Player: $player longest road length: ${longestRoadCoordinates.size}")
        }

        return longestRoads
    }

    // TODO: Mutable Set does not keep order
    private fun findLongestRoad(connectedRoads: MutableList<CatanRoadCoordinates>, roadPile: MutableSet<CatanRoadCoordinates>, playerColor: CatanColor):
            MutableList<CatanRoadCoordinates> {
        val recentRoad: CatanRoadCoordinates = connectedRoads.last()
        val connectableRoads = roadPile.filter {
            recentRoad != it &&
                    !connectedRoads.contains(it) &&
                    recentRoad.connectsTo(it) &&
                    !enemyVillageBlockingIntersection(recentRoad, it, playerColor)
        }.toMutableList()
        println("Road Pile: \n $roadPile")
        println("Connected Roads:\n $connectedRoads")
        println("Current Road:\n $recentRoad")
        println("Connectable:\n $connectableRoads")
        if(connectableRoads.isEmpty()) return connectedRoads

        var longestRoadCoordinates: MutableList<CatanRoadCoordinates> = mutableListOf()

        for (connected in connectableRoads) {
            val newConnectedRoadsList = connectedRoads.toMutableList()
            newConnectedRoadsList.add(connected)
            var currentRoadSizeMax = findLongestRoad(newConnectedRoadsList, roadPile, playerColor)

            if (currentRoadSizeMax.size > longestRoadCoordinates.size) {
                longestRoadCoordinates = currentRoadSizeMax
            }
        }

        return longestRoadCoordinates
    }

    // Todo: double check to make sure this is valid
    private fun enemyVillageBlockingIntersection(firstRoad: CatanRoadCoordinates, secondRoad: CatanRoadCoordinates, color: CatanColor): Boolean {
        return boardDataProvider.getBoardGamePieceLocations()[firstRoad.intersectingCoordinate(secondRoad)]?.color != color &&
                boardDataProvider.getBoardGamePieceLocations()[firstRoad.intersectingCoordinate(secondRoad)]?.color != null
    }
}