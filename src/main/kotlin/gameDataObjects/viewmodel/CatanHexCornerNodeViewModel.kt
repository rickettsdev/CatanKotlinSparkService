package gameDataObjects.viewmodel

import gameDataObjects.model.CatanHexCornerNode
import gameDataObjects.types.CatanGamePiece
import gameDataObjects.types.CatanResource

class CatanHexCornerNodeViewModel(private val model: CatanHexCornerNode) {
    var gamePiece: CatanGamePiece? = null

    fun getResources(): List<CatanResource> {
        return model.resources
    }

    fun getPortsIfAny(): CatanResource? {
        return model.portIfAny
    }
}