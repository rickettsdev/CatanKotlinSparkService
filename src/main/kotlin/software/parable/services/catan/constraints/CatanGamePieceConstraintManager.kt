package software.parable.services.catan.constraints

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanResource
import software.parable.services.catan.constraints.model.CatanPlayerGamePieceState
import software.parable.services.catan.constraints.model.CatanResourceDeckState

object CatanGamePieceConstraintManager {

    var resourceCardsAvailable = CatanResourceDeckState()
    var playerPieceState = mutableMapOf<CatanColor, CatanPlayerGamePieceState>()

    fun resetResourceCardDeck() {
        this.resourceCardsAvailable = CatanResourceDeckState()
    }

    fun redistributePlayerGamePieces(forPlayers: Set<CatanColor>) {
        playerPieceState.clear()
        for (player in forPlayers) {
            playerPieceState[player] = CatanPlayerGamePieceState()
        }
    }

    fun playerReceivedTheseCards(resourceCards: List<CatanResource>) {
        for (card in resourceCards) {
            removeCard(card)
        }
    }

    fun playerBuiltSettlement() {
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.WOOD)
        addCard(CatanResource.BRICK)
        addCard(CatanResource.SHEEP)
    }

    fun playerBuildCity() {
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.WHEAT)
        addCard(CatanResource.STONE)
        addCard(CatanResource.STONE)
    }

    fun playerBuildRoad() {
        addCard(CatanResource.BRICK)
        addCard(CatanResource.WOOD)
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
        }
    }
}