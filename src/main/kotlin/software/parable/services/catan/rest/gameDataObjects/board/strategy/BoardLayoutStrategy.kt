package gameDataObjects.board.strategy

import gameDataObjects.board.CatanBoardRequirements
import gameDataObjects.types.CatanColor

interface BoardLayoutStrategy {

    fun setupBoard(withPlayers: Set<CatanColor>): CatanBoardRequirements

    fun strategyImplementation(withPlayers: Set<CatanColor>): CatanBoardRequirements

}