package gameDataObjects.board

import gameDataObjects.board.manager.CatanGamePieceStateManager
import gameDataObjects.board.provider.CatanBoardDataProvider
import gameDataObjects.board.theme.CatanThemedPrinter
import gameDataObjects.board.ui.BoardPresenter
import gameDataObjects.model.CatanHexagonPieceModel
import gameDataObjects.types.*
import gameDataObjects.viewmodel.CatanHexCornerNodeViewModel
import java.lang.Exception

class CatanSecondBoard(
    private val hexCoordinateNodeViewModelMap: MutableMap<CatanCoordinate, CatanHexCornerNodeViewModel>,
    private val hexagonSubCoordinateMap: MutableMap<CatanCoordinate, List<CatanCoordinate>>,
    private val players: Set<CatanColor>,
    private val resourceMap: MutableMap<CatanCoordinate, CatanHexagonPieceModel>,
    hexPoints: Set<CatanCoordinate>,
    roadCoords: Set<CatanRoadCoordinates>,
    private val boardPresenter: BoardPresenter = BoardPresenter()
):
    CatanBoardRequirements, CatanBoardDataProvider {

    private var gamePieceLocations: MutableMap<CatanCoordinate, CatanGamePiece?> = mutableMapOf()
    private var roadPieceLocations: MutableMap<CatanRoadCoordinates, CatanColor?> = mutableMapOf()
    private var robberLocation: CatanCoordinate? = null

    private val gamePieceStateManager: CatanGamePieceStateManager

    init {
        for (subCoordinate in hexPoints) {
            gamePieceLocations[subCoordinate] = null
        }
        for (roadCoordinates in roadCoords) {
            roadPieceLocations[roadCoordinates] = CatanColor.UNASSIGNED
        }

        // Place the robber piece first
        for ((key, value) in resourceMap) {
            if (value.resource == CatanResource.DESERT) {
                println("Robber placed on coordinate: $key")
                robberLocation = key
                break
            }
        }
        if (robberLocation == null) {
            throw Exception("Robber was not placed. Board invalid.")
        }
        gamePieceStateManager = CatanGamePieceStateManager(this)
    }

    override fun printBoard() {
        boardPresenter.generateBoard(gamePieceLocations, roadPieceLocations, hexCoordinateNodeViewModelMap, resourceMap, robberLocation!!)
    }

    override fun numberRolled(number: Int): HashMap<CatanPlayer, MutableList<CatanResource>> {
        if (number == 7) {
            // TODO: throw robber exception
            return hashMapOf()
        }
        var playerResources: HashMap<CatanPlayer, MutableList<CatanResource>> = hashMapOf()
        for ((coordinate, model) in resourceMap) {
            if(model.diceRoll == number) {
                val subCoordinates = hexagonSubCoordinateMap[coordinate]!!
                for (subCoordinate in subCoordinates) {
                    if (gamePieceLocations[subCoordinate] != null) {
                        val resourceCards: MutableList<CatanResource> =
                            if (gamePieceLocations[subCoordinate]?.pieceType == CatanPiece.CITY) mutableListOf(model.resource, model.resource)
                            else mutableListOf(model.resource)
                        if (playerResources[CatanPlayer(gamePieceLocations[subCoordinate]!!.color)] == null) {
                            playerResources[CatanPlayer(gamePieceLocations[subCoordinate]!!.color)] = resourceCards
                        } else {
                            playerResources[CatanPlayer(gamePieceLocations[subCoordinate]!!.color)]!!.addAll(resourceCards)
                        }
                    }
                }
                // TODO: Check game pieces referenced from subcoordinate map.
            }
        }
        return playerResources
    }

    override fun moveRobber(coordinate: CatanCoordinate) {

        if (checkRobberCoordinateInbounds(coordinate)) {
            robberLocation = coordinate
        } else {
            throw Exception("Robber cannot be placed here!")
        }

    }

    override fun placeSettlement(subCoordinate: CatanCoordinate, gamePiece: CatanGamePiece) {
        if (gamePieceStateManager.isValidSettlementLocation(subCoordinate, gamePiece)) {
            gamePieceLocations[subCoordinate] = gamePiece
            hexCoordinateNodeViewModelMap[subCoordinate]?.gamePiece = gamePiece
        }
        else
            throw Exception("This is not a valid placement: ${gamePiece.pieceType}, ${gamePiece.color} at coordinate $subCoordinate")
    }

    override fun placeRoad(roadCoordinates: CatanRoadCoordinates, color: CatanColor) {
        val emptyRoadSlot = roadPieceLocations[roadCoordinates] == CatanColor.UNASSIGNED
        println(roadPieceLocations[roadCoordinates])
        val isValidRoadPlacement = gamePieceStateManager.isValidRoadPlacement(roadCoordinates, color)

        println("Empty Road: ${emptyRoadSlot}, isValidRoadPlacement: $isValidRoadPlacement")

        if (emptyRoadSlot && isValidRoadPlacement)
            roadPieceLocations[roadCoordinates] = color
        else
            throw Exception("Empty Road: ${emptyRoadSlot}, isValidRoadPlacement: $isValidRoadPlacement")
    }

    // TODO: Check to see if road is longer than 5
    override fun getVictoryPoints(): HashMap<CatanColor, Int> {
        val longestRoads = gamePieceStateManager.provideLongestRoad()
        val playerVictoryPoints = hashMapOf<CatanColor, Int>()

        for (color in players) {
            playerVictoryPoints[color] = 0
        }

        var longestRoad = 0
        var longestRoadColor: CatanColor? = null

        for ((key, value) in longestRoads) {
            println("key: $key value: $value")
            if (value.size > longestRoad) {
                longestRoad = value.size
                longestRoadColor = key
            }
        }
        if (longestRoadColor != null) {
            playerVictoryPoints[longestRoadColor] = 2
        }

        return playerVictoryPoints
    }

    override fun playerHasPortAccess(player: CatanPlayer, portResource: CatanPortType) {
        TODO("Not yet implemented")
    }

    private fun checkRobberCoordinateInbounds(coordinate: CatanCoordinate): Boolean {
        return resourceMap.keys.contains(coordinate)
    }

    /**
     * Implementation of CatanBoardDataProvider
     * */
    override fun getBoardGamePieceLocations(): MutableMap<CatanCoordinate, CatanGamePiece?> {
        return gamePieceLocations
    }

    override fun getBoardRoadPieceLocations(): MutableMap<CatanRoadCoordinates, CatanColor?> {
        return roadPieceLocations
    }

    override fun getBoardPlayers(): Set<CatanColor> {
        return players
    }
}