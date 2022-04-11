package software.parable.gameflow

import gameDataObjects.types.CatanColor
import software.parable.services.catan.gameflow.CatanTurnManager
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class CatanTurnManagerTest {

    companion object {
        val PLAYER_ORDERING = setOf(CatanColor.RED, CatanColor.BLUE, CatanColor.YELLOW, CatanColor.WHITE)
    }

    private val turnManager = CatanTurnManager

    @BeforeTest
    fun beforeTest() {
        turnManager.startGame(PLAYER_ORDERING)
    }

    @Test
    fun endTurnCorrectIndexes() {
        //given
        //when
        turnManager.endTurn()
        //then
        assertEquals(turnManager.turnState.playerTurn, CatanColor.BLUE)
    }

    @Test
    fun endTurn3TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..2) {
            turnManager.endTurn()
        }
        //then
        assertEquals(turnManager.turnState.playerTurn, CatanColor.WHITE)
    }
    @Test
    fun endTurn4TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..3) {
            turnManager.endTurn()
        }
        //then
        assertEquals(turnManager.turnState.playerTurn, CatanColor.RED)
    }

    //
    @Test
    fun endTurn7TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..6) {
            turnManager.endTurn()
        }
        //then
        assertEquals(turnManager.turnState.playerTurn, CatanColor.WHITE)
    }
}