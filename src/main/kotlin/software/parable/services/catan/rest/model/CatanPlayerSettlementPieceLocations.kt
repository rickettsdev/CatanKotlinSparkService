package software.parable.services.catan.rest.model

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanCoordinate
import gameDataObjects.types.CatanGamePiece

typealias SettlementLocationsModel = HashMap<String, String>

class CatanPlayerSettlementPieceLocations:
        CatanJacksonModelResponse<MutableMap<CatanCoordinate, CatanGamePiece?>, String, List<SettlementLocationsModel>>(){

    override fun createJacksonFriendlyModel(model: MutableMap<CatanCoordinate, CatanGamePiece?>): HashMap<String, List<SettlementLocationsModel>> {
        return hashMapOf(
            "red" to getSettlementModels(model, CatanColor.RED),
            "blue" to getSettlementModels(model, CatanColor.BLUE),
            "white" to getSettlementModels(model, CatanColor.WHITE),
            "yellow" to getSettlementModels(model, CatanColor.YELLOW)
        )
    }

    private fun getSettlementModels(model: MutableMap<CatanCoordinate, CatanGamePiece?>, ofColor: CatanColor):
            List<SettlementLocationsModel> {
        return model.keys.filter { key -> model[key]?.color == ofColor }
            .map { key -> hashMapOf(
                "x" to key.x.toString(),
                "y" to key.y.toString(),
                "type" to model[key]!!.pieceType.name.substring(0, 1) // If there is an issue with city/village pieces in the future, this is why
            ) }
    }
}