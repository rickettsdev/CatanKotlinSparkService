package gameDataObjects.factory

import gameDataObjects.types.CatanResource

class CatanResourceHexagonTileFactory(randomize: Boolean = false) {
    private val availableHexagons: List<CatanResource> = listOf(
        CatanResource.STONE, CatanResource.STONE, CatanResource.STONE,
        CatanResource.BRICK,CatanResource.BRICK,CatanResource.BRICK, CatanResource.WOOD,
        CatanResource.WOOD, CatanResource.WOOD, CatanResource.WOOD, CatanResource.WHEAT, CatanResource.WHEAT,
        CatanResource.WHEAT, CatanResource.WHEAT, CatanResource.SHEEP, CatanResource.SHEEP,
        CatanResource.SHEEP, CatanResource.SHEEP, CatanResource.DESERT
    )

    private val inUseHexagons =
        if(randomize) availableHexagons.shuffled().toMutableList()
        else availableHexagons.toMutableList()

    //.shuffled().toMutableList()

    fun getResourceTile(): CatanResource {
        // pop!
        println("${inUseHexagons.size}")
        return inUseHexagons.removeAt(0)
    }
}