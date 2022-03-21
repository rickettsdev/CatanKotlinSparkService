package org.example

import gameDataObjects.board.strategy.CatanBoardLayoutStrategyFirst
import gameDataObjects.factory.CatanNumberCirclePieceFactory
import gameDataObjects.factory.CatanResourceHexagonTileFactory
import gameDataObjects.types.*
import java.lang.Exception
import kotlin.test.Test
import kotlin.test.AfterTest
import kotlin.test.assertEquals

internal class CatanGameDataObjectsTest {
    companion object {
        val BLUE_PLAYER = CatanPlayer(CatanColor.BLUE)
        val RED_PLAYER = CatanPlayer(CatanColor.RED)
    }

    var boardStrategy =
        CatanBoardLayoutStrategyFirst(
            CatanResourceHexagonTileFactory(),
            CatanNumberCirclePieceFactory()
        )
    var board = boardStrategy.strategyImplementation(setOf(CatanColor.BLUE, CatanColor.RED))

    @AfterTest
    fun afterTest() {
        boardStrategy =
            CatanBoardLayoutStrategyFirst(
                CatanResourceHexagonTileFactory(),
                CatanNumberCirclePieceFactory()
            )
        board = boardStrategy.strategyImplementation(setOf(CatanColor.BLUE, CatanColor.RED))
    }

    @Test
    fun numberRolled() {
        //given
        board.placeSettlement(
            CatanCoordinate(0,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        //when
        val result = board.numberRolled(2)

        //then
        val expectedResult = hashMapOf(BLUE_PLAYER to mutableListOf(CatanResource.STONE))
        assertEquals(expectedResult, result, "")
    }
    @Test
    fun robberRolled() {
        //given
        board.placeSettlement(
            CatanCoordinate(0,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.moveRobber(CatanCoordinate(0,0))

        //when
        val result = board.numberRolled(7)

        //then
        val expectedResult: HashMap<CatanPlayer, MutableList<CatanResource>> = hashMapOf()
        assertEquals(expectedResult, result, "")

        board.printBoard()
    }

    @Test
    fun sharedResourceRoll() {
        //given
        board.placeSettlement(
            CatanCoordinate(0,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(1,3),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        //when
        val result = board.numberRolled(2)

        //then
        val expectedResult = hashMapOf(
            BLUE_PLAYER to mutableListOf(CatanResource.STONE),
            RED_PLAYER to mutableListOf(CatanResource.STONE)
        )
        assertEquals(expectedResult, result, "")
    }

    @Test
    fun sharedResourceRollRedGetsExtra() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        //when
        val result = board.numberRolled(3)

        //then
        val expectedResult = hashMapOf(
            BLUE_PLAYER to mutableListOf(CatanResource.STONE),
            RED_PLAYER to mutableListOf(CatanResource.STONE, CatanResource.STONE)
        )
        assertEquals(expectedResult, result, "")
    }

    @Test
    fun blueSettlementRedCityRoll() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.CITY
            )
        )

        //when
        val result = board.numberRolled(3)

        //then
        val expectedResult = hashMapOf(
            BLUE_PLAYER to mutableListOf(CatanResource.STONE),
            RED_PLAYER to mutableListOf(CatanResource.STONE, CatanResource.STONE, CatanResource.STONE, CatanResource.STONE)
        )
        assertEquals(expectedResult, result, "")
    }

    @Test
    fun blueSettlementRedRoadPlacement() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.CITY
            )
        )

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)

        //then no exception is thrown
    }

    @Test
    fun blueSettlementTwoRedRoadPlacement() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.CITY
            )
        )

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)


        //then no exception is thrown
    }

    @Test(expected = Exception::class)
    fun blueSettlementNotRealRedRoadPlacement() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.CITY
            )
        )

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,5))), CatanColor.RED)


        //then exception is thrown. The road does not connect
    }

    @Test(expected = Exception::class)
    fun blueSettlementTwoRedRoadDisjointPlacement() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.CITY
            )
        )

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,4), CatanCoordinate(3,5))), CatanColor.RED)


        //then exception is thrown. The road does not connect
    }

    @Test(expected = Exception::class)
    fun blueSettlementInvalidRedRoadPlacement() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)

        //then exception is thrown
    }

    @Test(expected = Exception::class)
    fun redRoadPlacementBlueSettlementBetween() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.CITY
            )
        )

        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)

        board.placeSettlement(
            CatanCoordinate(3,4),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.printBoard()

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,4), CatanCoordinate(3,5))), CatanColor.RED)

        //then exception is thrown. blue village in middle
    }

    @Test
    fun validLoopAroundRedRoadPlacementBlueSettlementBetween() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(2,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,3), CatanCoordinate(2,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,4), CatanCoordinate(3,5))), CatanColor.RED)

        board.placeSettlement(
            CatanCoordinate(3,4),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,4), CatanCoordinate(3,5))), CatanColor.RED)

        //then exception is not thrown
        board.printBoard()
    }

    @Test(expected = Exception::class)
    fun incompleteLoopAroundRedRoadPlacementBlueSettlementBetween() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(2,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,3), CatanCoordinate(2,4))), CatanColor.RED)

        board.placeSettlement(
            CatanCoordinate(3,4),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.printBoard()

        //when
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,4), CatanCoordinate(3,5))), CatanColor.RED)

        //then exception thrown
    }

    @Test(expected = Exception::class)
    fun blueSettlementRedSettlementTooClose() {
        //given
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        //when
        board.placeSettlement(
            CatanCoordinate(1,1),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        //then exception is thrown. settlement too close
    }

    @Test
    fun validRobberPlacement() {
        //given
        val validRobberCoordinate: CatanCoordinate = CatanCoordinate(0,0)

        //when
        board.moveRobber(validRobberCoordinate)

        //then no exception thrown
    }

    @Test(expected = Exception::class)
    fun invalidRobberPlacement() {
        //given
        val validRobberCoordinate: CatanCoordinate = CatanCoordinate(-1,0)

        //when
        board.moveRobber(validRobberCoordinate)

        //then exception thrown
    }

    @Test
    fun redLongestRoad() {
        //given
        val expectedRedVictoryPoints = 2

        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(2,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,3), CatanCoordinate(2,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,4), CatanCoordinate(3,5))), CatanColor.RED)

        board.placeSettlement(
            CatanCoordinate(3,4),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.printBoard()

        //when
        val victoryPoints = board.getVictoryPoints()

        //then
        assertEquals(expectedRedVictoryPoints, victoryPoints[CatanColor.RED], "$victoryPoints")
    }

    @Test
    fun blueLongestRoad() {
        //given
        val expectedRedVictoryPoints = 2

        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )

        //Red roads
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(2,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,3), CatanCoordinate(2,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,4), CatanCoordinate(3,5))), CatanColor.RED)

        //Blue roads
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,0), CatanCoordinate(1,1))), CatanColor.BLUE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,1), CatanCoordinate(1,2))), CatanColor.BLUE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,2), CatanCoordinate(1,3))), CatanColor.BLUE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,3), CatanCoordinate(1,4))), CatanColor.BLUE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,4), CatanCoordinate(1,5))), CatanColor.BLUE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,5), CatanCoordinate(1,6))), CatanColor.BLUE)

        board.printBoard()

        //when
        val victoryPoints = board.getVictoryPoints()

        //then
        assertEquals(expectedRedVictoryPoints, victoryPoints[CatanColor.BLUE], "$victoryPoints")
    }
    @Test
    fun printBoard() {
        board.placeSettlement(
            CatanCoordinate(1,0),
            CatanGamePiece(CatanColor.RED,
                CatanPiece.SETTLEMENT
            )
        )
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,0), CatanCoordinate(1,1))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,1), CatanCoordinate(1,2))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,2), CatanCoordinate(1,3))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,3), CatanCoordinate(1,4))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,4), CatanCoordinate(1,5))), CatanColor.RED)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1,5), CatanCoordinate(1,6))), CatanColor.RED)

        board.placeSettlement(
            CatanCoordinate(0,9),
            CatanGamePiece(CatanColor.BLUE,
                CatanPiece.SETTLEMENT
            )
        )
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(0, 9), CatanCoordinate(0, 10))), CatanColor.BLUE)

        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(0, 10), CatanCoordinate(0, 11))), CatanColor.BLUE)

        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(1, 10), CatanCoordinate(0, 11))), CatanColor.BLUE)

        board.placeSettlement(
            CatanCoordinate(2,2),
            CatanGamePiece(CatanColor.YELLOW,
                CatanPiece.SETTLEMENT
            )
        )
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.YELLOW)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.YELLOW)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(2,3))), CatanColor.YELLOW)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,3), CatanCoordinate(2,4))), CatanColor.YELLOW)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,4), CatanCoordinate(3,5))), CatanColor.YELLOW)

        board.placeSettlement(
            CatanCoordinate(3,7),
            CatanGamePiece(CatanColor.WHITE,
                CatanPiece.SETTLEMENT
            )
        )
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,7), CatanCoordinate(3,8))), CatanColor.WHITE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,8), CatanCoordinate(3,9))), CatanColor.WHITE)
        board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(4,8), CatanCoordinate(3,9) )), CatanColor.WHITE)

        board.printBoard()
    }
}
