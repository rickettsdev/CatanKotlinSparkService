package software.parable.services.catan.rest.model

import gameDataObjects.model.CatanHexagonPieceModel
import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanCoordinate

typealias ResourceTileLocationModel = HashMap<String, String>

class CatanPlayerResourceTileLocations: CatanJacksonModelResponse
<MutableMap<CatanCoordinate, CatanHexagonPieceModel>,
        String,
        List<ResourceTileLocationModel>>() {
    override fun createJacksonFriendlyModel(model: MutableMap<CatanCoordinate, CatanHexagonPieceModel>): HashMap<String, List<ResourceTileLocationModel>> {
        val resourceTileModelList = model.keys.map { key -> hashMapOf(
            "x" to key.x.toString(),
            "y" to key.y.toString(),
            "resource" to "${model[key]!!.resource.name}",
            "diceRoll" to "${model[key]!!.diceRoll}"
        )}
        return hashMapOf(
            "resources" to resourceTileModelList
        )
    }
}