package software.parable.constraints

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanResource
import software.parable.services.catan.constraints.CatanGamePieceConstraintManager
import java.lang.Exception
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CatanGamePieceConstraintManagerTest {
    companion object {
        val PLAYER_SET = setOf(CatanColor.RED, CatanColor.BLUE, CatanColor.YELLOW, CatanColor.WHITE)
    }

    private val constraintManager = CatanGamePieceConstraintManager

    @BeforeTest
    fun beforeTest() {
        constraintManager.resetItAll(PLAYER_SET)
    }

    @Test
    fun baseCaseResourceCardsAvailable() {
        // given
        val expectedCardCount = 19
        // when

        // then
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.brickCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.wheatCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.woodCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.sheepCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.stoneCount)
    }

    @Test
    fun playerReceivedOneOfEveryResource() {
        // given
        val cardsReceived = listOf(
            CatanResource.STONE, CatanResource.SHEEP, CatanResource.WOOD, CatanResource.BRICK, CatanResource.WHEAT
        )
        val expectedCardCount = 18
        // when
        constraintManager.playerReceivedTheseCards(cardsReceived)
        // then
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.brickCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.wheatCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.woodCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.sheepCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.stoneCount)
    }

    @Test
    fun playerReceivedAllExceptSheep() {
        // given
        val cardsReceived = listOf(
            CatanResource.STONE, CatanResource.WOOD, CatanResource.BRICK, CatanResource.WHEAT
        )
        val expectedSheepCount = 19
        val expectedCardCount = 18
        // when
        constraintManager.playerReceivedTheseCards(cardsReceived)
        // then
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.brickCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.wheatCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.woodCount)
        assertEquals(expectedSheepCount, constraintManager.resourceCardsAvailable.sheepCount)
        assertEquals(expectedCardCount, constraintManager.resourceCardsAvailable.stoneCount)
    }

    @Test
    fun redPlayerCanBuildSettlement() {
        // given
        constraintManager.playerReceivedTheseCards(listOf(CatanResource.BRICK, CatanResource.WOOD, CatanResource.SHEEP, CatanResource.WHEAT))
        constraintManager.playerPieceState[CatanColor.BLUE]!!.settlementPieceCount = 0
        // when
        constraintManager.playerBuildRoad(CatanColor.RED)
        // then no exception is thrown
    }

    @Test
    fun redPlayerCanBuildCity() {
        // given
        constraintManager.playerReceivedTheseCards(listOf(CatanResource.WHEAT, CatanResource.WHEAT, CatanResource.WHEAT, CatanResource.STONE, CatanResource.STONE))
        constraintManager.playerPieceState[CatanColor.BLUE]!!.cityPieceCount = 0
        // when
        constraintManager.playerBuildCity(CatanColor.RED)
        // then no exception is thrown
    }

    @Test
    fun redPlayerCanBuildRoad() {
        // given
        constraintManager.playerReceivedTheseCards(listOf(CatanResource.WOOD, CatanResource.BRICK))
        constraintManager.playerPieceState[CatanColor.BLUE]!!.roadPieceCount = 0
        // when
        constraintManager.playerBuildRoad(CatanColor.RED)
        // then no exception is thrown
    }

    @Test(expected = Exception::class)
    fun bluePlayerCannotBuildCity() {
        // given
        constraintManager.playerReceivedTheseCards(listOf(CatanResource.WHEAT, CatanResource.WHEAT, CatanResource.WHEAT, CatanResource.STONE, CatanResource.STONE))
        constraintManager.playerPieceState[CatanColor.BLUE]!!.cityPieceCount = 0
        // when
        constraintManager.playerBuildCity(CatanColor.BLUE)
        // then exception is thrown
    }

    @Test(expected = Exception::class)
    fun bluePlayerCannotBuildRoad() {
        // given
        constraintManager.playerReceivedTheseCards(listOf(CatanResource.WOOD, CatanResource.BRICK))
        constraintManager.playerPieceState[CatanColor.BLUE]!!.roadPieceCount = 0
        // when
        constraintManager.playerBuildRoad(CatanColor.BLUE)
        // then exception is thrown
    }

    @Test(expected = Exception::class)
    fun bluePlayerCannotBuildSettlement() {
        // given
        constraintManager.playerReceivedTheseCards(listOf(CatanResource.BRICK, CatanResource.WOOD, CatanResource.SHEEP, CatanResource.WHEAT))
        constraintManager.playerPieceState[CatanColor.BLUE]!!.settlementPieceCount = 0
        // when
        constraintManager.playerBuiltSettlement(CatanColor.BLUE)
        // then exception is thrown
    }
}