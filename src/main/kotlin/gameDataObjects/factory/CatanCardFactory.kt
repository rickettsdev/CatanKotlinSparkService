package gameDataObjects.factory

import gameDataObjects.types.CatanCardType

class CatanCardFactory {
    private val cards: List<CatanCardType> = listOf(
        CatanCardType.KNIGHT, CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,
        CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,
        CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,CatanCardType.KNIGHT,
        CatanCardType.PLACE_TWO_ROADS, CatanCardType.PLACE_TWO_ROADS,
        CatanCardType.TAKE_ANY_TWO_RESOURCES_BANK, CatanCardType.TAKE_ANY_TWO_RESOURCES_BANK,
        CatanCardType.MONOPOLY, CatanCardType.MONOPOLY,
        CatanCardType.VICTORY_POINT, CatanCardType.VICTORY_POINT, CatanCardType.VICTORY_POINT, CatanCardType.VICTORY_POINT, CatanCardType.VICTORY_POINT,
    )

    fun getDevelopmentCards(): List<CatanCardType> {
        return cards
    }
}