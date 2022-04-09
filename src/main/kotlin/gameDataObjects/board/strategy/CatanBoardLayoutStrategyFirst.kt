package gameDataObjects.board.strategy

import gameDataObjects.board.CatanBoardRequirements
import gameDataObjects.model.CatanHexCornerNode
import gameDataObjects.board.CatanSecondBoard
import gameDataObjects.constants.BoardSetupConstants
import gameDataObjects.factory.CatanNumberCirclePieceFactory
import gameDataObjects.factory.CatanResourceHexagonTileFactory
import gameDataObjects.model.CatanHexagonPieceModel
import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanCoordinate
import gameDataObjects.types.CatanResource
import gameDataObjects.types.CatanRoadCoordinates
import gameDataObjects.viewmodel.CatanHexCornerNodeViewModel

class CatanBoardLayoutStrategyFirst(
    private val hexagonResourceTileFactory: CatanResourceHexagonTileFactory,
    private val circleTileNumberFactory: CatanNumberCirclePieceFactory,
) : BoardLayoutStrategy {

    private var hexCoordinateSet: MutableSet<CatanCoordinate> = mutableSetOf()
    private var hexCoordinateNodeViewModelMap: MutableMap<CatanCoordinate, CatanHexCornerNodeViewModel> = mutableMapOf()
    private var validRoadCoordinates: MutableSet<CatanRoadCoordinates> = mutableSetOf()
    private var hexagonSubCoordinateMap: MutableMap<CatanCoordinate, List<CatanCoordinate>> = mutableMapOf()
    private var resourceMap: MutableMap<CatanCoordinate, CatanHexagonPieceModel> = mutableMapOf()

    override fun setupBoard(withPlayers: Set<CatanColor>): CatanBoardRequirements {
        return strategyImplementation(withPlayers)
    }

    override fun strategyImplementation(withPlayers: Set<CatanColor>): CatanSecondBoard {
        populateHexCornerNodeSet()
        populateValidRoads(CatanCoordinate(0,0))
        populateValidRoads(CatanCoordinate(1, 0))
        populateValidRoads(CatanCoordinate(2, 0))

        for(road in validRoadCoordinates) {
            println("Road: $road")
        }

        return CatanSecondBoard(
            hexCoordinateNodeViewModelMap,
            hexagonSubCoordinateMap,
            withPlayers,
            resourceMap,
            hexCoordinateSet,
            validRoadCoordinates,
        )
    }

    private fun getRootNode(hexCoordinate: CatanCoordinate): CatanCoordinate {
        if(hexCoordinate.y == 1) return CatanCoordinate(hexCoordinate.x, hexCoordinate.y+1)
        if(hexCoordinate.y == 2) return CatanCoordinate(hexCoordinate.x, hexCoordinate.y+2)
        if(hexCoordinate.y == 3) return CatanCoordinate(hexCoordinate.x + 1, hexCoordinate.y + 3)
        if (hexCoordinate.y == 4) return CatanCoordinate(hexCoordinate.x + 1, hexCoordinate.y + 4)
        return hexCoordinate
    }

    private fun getFollowingFiveNodesFromRootNode(hexCoordinate: CatanCoordinate, rootCoordinate: CatanCoordinate): List<CatanCoordinate> {
        if(listOf(0, 1).contains(hexCoordinate.y)) {
            return listOf(
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+1),
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+2),
                CatanCoordinate(rootCoordinate.x+1, rootCoordinate.y+1),
                CatanCoordinate(rootCoordinate.x+1, rootCoordinate.y+2),
                CatanCoordinate(rootCoordinate.x+1, rootCoordinate.y+3)
            )
        }
        else if (2 == hexCoordinate.y) {
            return listOf(
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+1),
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+2),
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+3),
                CatanCoordinate(rootCoordinate.x+1, rootCoordinate.y+1),
                CatanCoordinate(rootCoordinate.x+1, rootCoordinate.y+2),
            )
        }
        else if (listOf(3, 4).contains(hexCoordinate.y)) {
            return listOf(
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+1),
                CatanCoordinate(rootCoordinate.x, rootCoordinate.y+2),
                CatanCoordinate(rootCoordinate.x-1, rootCoordinate.y+1),
                CatanCoordinate(rootCoordinate.x-1, rootCoordinate.y+2),
                CatanCoordinate(rootCoordinate.x-1, rootCoordinate.y+3),
            )
        }
        return listOf()
    }
    private fun populateHexCornerNodeSet() {
        println("HERE")
        for(hexagonPiece in BoardSetupConstants.VALID_COORDINATES) {
            val resource = hexagonResourceTileFactory.getResourceTile()
            val roll = if (CatanResource.DESERT == resource) 7 else circleTileNumberFactory.getNumberTile()

            resourceMap[hexagonPiece] = CatanHexagonPieceModel(resource, roll)

            val viewModel = CatanHexCornerNodeViewModel(CatanHexCornerNode(listOf(resource), null))
            println("in coordinate: $hexagonPiece")
            val rootCoordinate = getRootNode(hexagonPiece)
            hexCoordinateSet.add(rootCoordinate)
            val otherFive = getFollowingFiveNodesFromRootNode(hexagonPiece, rootCoordinate)
            //TODO: Below line needs redone. No need for resources in hex coordinate node map
            for (node in otherFive) hexCoordinateNodeViewModelMap[node] = CatanHexCornerNodeViewModel(CatanHexCornerNode(listOf(resource), null))
            hexCoordinateSet.addAll(otherFive)

            updateHexCoordinateViewModelMap(rootCoordinate, viewModel)

            updateHexToHexCornerCoordinateMap(hexagonPiece, rootCoordinate, otherFive)
        }

        //debugging
        for(coordinate in hexCoordinateSet.sortedBy { it.y }) {
            println("$coordinate")
        }

        for((coordinate, viewModel) in hexCoordinateNodeViewModelMap) {
            println("Coordinate: ${coordinate}, resources, ${viewModel.getResources()}")
        }

        for ((coordinate, subCoordinates) in hexagonSubCoordinateMap) {
            println("Hexagon Coordinate: $coordinate , subcoordinates: $subCoordinates")
        }
    }

    private fun updateHexCoordinateViewModelMap(rootCoordinate: CatanCoordinate, viewModel: CatanHexCornerNodeViewModel) {
        hexCoordinateNodeViewModelMap[rootCoordinate] = viewModel
    }

    private fun updateHexToHexCornerCoordinateMap(hexCoordinate: CatanCoordinate, rootCoordinate: CatanCoordinate, coordinates: List<CatanCoordinate>) {
        val coordinatesList: List<CatanCoordinate> = coordinates.plus(rootCoordinate)
        hexagonSubCoordinateMap[hexCoordinate] = coordinatesList
    }
    //TODO: May be something wrong with this.
    private fun populateValidRoads(rootNode: CatanCoordinate) {
        if (rootNode.y == 11) {
            return
        }
        else if (rootNode.y % 2 == 0) {
            val leftNode: CatanCoordinate? =
                if(rootNode.y >= BoardSetupConstants.LIST_UPPER_BOUND && rootNode.x == 0) null
                else if (rootNode.y >= BoardSetupConstants.LIST_UPPER_BOUND) CatanCoordinate(rootNode.x - 1, rootNode.y+1)
                else CatanCoordinate(rootNode.x, rootNode.y+1)
            val rightNode: CatanCoordinate? =
                if(rootNode.y >= BoardSetupConstants.LIST_UPPER_BOUND && rootNode.x == hexCoordinateSet.filter { it.y == rootNode.y }.size - 1) null
                else if (rootNode.y >= BoardSetupConstants.LIST_UPPER_BOUND) CatanCoordinate(rootNode.x, rootNode.y+1)
                else CatanCoordinate(rootNode.x + 1, rootNode.y + 1)
            if (leftNode != null) {
                validRoadCoordinates.add(CatanRoadCoordinates(listOf(rootNode, leftNode)))
                populateValidRoads(leftNode)
            }
            if (rightNode != null) {
                validRoadCoordinates.add(CatanRoadCoordinates(listOf(rootNode, rightNode)))
                populateValidRoads(rightNode)
            }
        }
        else {
            val coordinateBelow = CatanCoordinate(rootNode.x, rootNode.y+1)
            validRoadCoordinates.add(CatanRoadCoordinates(listOf(rootNode, coordinateBelow)))
            populateValidRoads(coordinateBelow)
        }
    }
}