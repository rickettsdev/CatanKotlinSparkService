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
        assertEquals(CatanColor.BLUE, turnManager.turnState.playerTurn)
    }

    @Test
    fun endTurn3TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..2) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.WHITE, turnManager.turnState.playerTurn)
    }
    @Test
    fun endTurn4TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..3) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.WHITE, turnManager.turnState.playerTurn)
    }

    @Test
    fun endTurn7TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..6) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.RED, turnManager.turnState.playerTurn)
    }

    //
    @Test
    fun endTurn8TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..7) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.RED, turnManager.turnState.playerTurn)
    }

    @Test
    fun endTurn9TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..8) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.BLUE, turnManager.turnState.playerTurn)
    }

    @Test
    fun endTurn12TimesCorrectIndexes() {
        //given
        //when
        for (i in 0..11) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.RED, turnManager.turnState.playerTurn)
    }

    // 3 Players tests

    @Test
    fun endTurn3TimesCorrectIndexes3Players() {
        //given
        turnManager.startGame(setOf(CatanColor.RED, CatanColor.BLUE, CatanColor.YELLOW))
        //when
        for (i in 0..2) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.YELLOW, turnManager.turnState.playerTurn)
    }

    @Test
    fun endTurn4TimesCorrectIndexes3Players() {
        //given
        turnManager.startGame(setOf(CatanColor.RED, CatanColor.BLUE, CatanColor.YELLOW))
        //when
        for (i in 0..3) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.BLUE, turnManager.turnState.playerTurn)
    }

    @Test
    fun endTurn6TimesCorrectIndexes3Players() {
        //given
        turnManager.startGame(setOf(CatanColor.RED, CatanColor.BLUE, CatanColor.YELLOW))
        //when
        for (i in 0..5) {
            turnManager.endTurn()
        }
        //then
        assertEquals(CatanColor.RED, turnManager.turnState.playerTurn)
    }
}