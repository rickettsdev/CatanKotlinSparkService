package software.parable.services.catan.constraints

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanResource
import software.parable.services.catan.constraints.model.CatanPlayerGamePieceState
import software.parable.services.catan.constraints.model.CatanResourceDeckState

object CatanGamePieceConstraintManager {

    var resourceCardsAvailable = CatanResourceDeckState()
    var playerPieceState = mutableMapOf<CatanColor, CatanPlayerGamePieceState>()
    var mostRecentPlayersSaved = setOf<CatanColor>()

    fun resetItAll(forPlayers: Set<CatanColor> = mostRecentPlayersSaved, maxResourceCount: Int = 19) {
        resetResourceCardDeck(maxResourceCount)
        redistributePlayerGamePieces(forPlayers)
    }

    fun resetResourceCardDeck(maxResourceCount: Int = 19) {
        this.resourceCardsAvailable = CatanResourceDeckState(maxResourceCount)
    }

    fun redistributePlayerGamePieces(forPlayers: Set<CatanColor>) {
        playerPieceState.clear()
        for (player in forPlayers) {
            playerPieceState[player] = CatanPlayerGamePieceState()
        }
        mostRecentPlayersSaved = forPlayers
    }

    fun playerReceivedTheseCards(resourceCards: List<CatanResource>) {
        for (card in resourceCards) {
            removeCard(card)
        }
    }

    fun playerBuiltSettlement(color: CatanColor) {
        if(playerPieceState[color]!!.settlementPieceCount < 1) throw Exception("Not enough settlements M'Lord.")
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.WOOD)
        addCard(CatanResource.BRICK)
        addCard(CatanResource.SHEEP)
        playerPieceState[color]!!.settlementPieceCount--
    }

    fun playerBuildCity(color: CatanColor) {
        if(playerPieceState[color]!!.cityPieceCount < 1) throw Exception("Not enough cities M'Lord.")
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.STONE)
        addCard(CatanResource.STONE)
        playerPieceState[color]!!.cityPieceCount--
    }

    fun playerBuildRoad(color: CatanColor) {
        if(playerPieceState[color]!!.roadPieceCount < 1) throw Exception("Not enough roads M'Lord.")
        addCard(CatanResource.BRICK)
        addCard(CatanResource.WOOD)
        playerPieceState[color]!!.roadPieceCount--
    }

    private fun addCard(type: CatanResource){
        when (type) {
            CatanResource.BRICK ->
                if(resourceCardsAvailable.brickCount < resourceCardsAvailable.maxCardCount)
                    resourceCardsAvailable.brickCount++ else throw Exception("Max Card Limit")
            CatanResource.STONE ->
                if(resourceCardsAvailable.stoneCount < resourceCardsAvailable.maxCardCount)
                    resourceCardsAvailable.stoneCount++ else throw Exception("Max Card Limit")
            CatanResource.WHEAT ->
                if(resourceCardsAvailable.wheatCount < resourceCardsAvailable.maxCardCount)
                    resourceCardsAvailable.wheatCount++ else throw Exception("Max Card Limit")
            CatanResource.WOOD ->
                if(resourceCardsAvailable.woodCount < resourceCardsAvailable.maxCardCount)
                    resourceCardsAvailable.woodCount++ else throw Exception("Max Card Limit")
            CatanResource.SHEEP ->
                if(resourceCardsAvailable.sheepCount < resourceCardsAvailable.maxCardCount)
                    resourceCardsAvailable.sheepCount++ else throw Exception("Max Card Limit")
            CatanResource.DESERT, CatanResource.THREE_FOR_ONE_PORT -> throw Exception("Not a card.")
        }
    }
    private fun removeCard(type: CatanResource){
        when (type) {
            CatanResource.BRICK ->
                if(resourceCardsAvailable.brickCount > 0)
                    resourceCardsAvailable.brickCount-- else throw Exception("Not Enough Cards M'Lord")
            CatanResource.STONE ->
                if(resourceCardsAvailable.stoneCount > 0)
                    resourceCardsAvailable.stoneCount-- else throw Exception("Not Enough Cards M'Lord")
            CatanResource.WHEAT ->
                if(resourceCardsAvailable.wheatCount > 0)
                    resourceCardsAvailable.wheatCount-- else throw Exception("Not Enough Cards M'Lord")
            CatanResource.WOOD ->
                if(resourceCardsAvailable.woodCount > 0)
                    resourceCardsAvailable.woodCount-- else throw Exception("Not Enough Cards M'Lord")
            CatanResource.SHEEP ->
                if(resourceCardsAvailable.sheepCount > 0)
                    resourceCardsAvailable.sheepCount-- else throw Exception("Not Enough Cards M'Lord")
            CatanResource.DESERT, CatanResource.THREE_FOR_ONE_PORT -> throw Exception("Not a card.")
        }
    }
}