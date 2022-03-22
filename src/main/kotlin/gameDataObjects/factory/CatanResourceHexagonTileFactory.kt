package gameDataObjects.factory

import gameDataObjects.types.CatanResource

class CatanResourceHexagonTileFactory {
    var availableHexagons: MutableList<CatanResource> = mutableListOf(
                    CatanResource.STONE, CatanResource.STONE, CatanResource.STONE,
                CatanResource.BRICK,CatanResource.BRICK,CatanResource.BRICK, CatanResource.WOOD,
          CatanResource.WOOD, CatanResource.WOOD, CatanResource.WOOD, CatanResource.WHEAT, CatanResource.WHEAT,
                CatanResource.WHEAT, CatanResource.WHEAT, CatanResource.SHEEP, CatanResource.SHEEP,
                    CatanResource.SHEEP, CatanResource.SHEEP, CatanResource.DESERT
    )

    fun getResourceTile(): CatanResource {
        // pop!
        println("${availableHexagons.size}")
        return availableHexagons.removeAt(0)
    }
}