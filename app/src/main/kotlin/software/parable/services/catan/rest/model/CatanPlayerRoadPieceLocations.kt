package software.parable.services.catan.rest.model

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanRoadCoordinates

typealias RoadLocationsModel = HashMap<String, Int>

class CatanPlayerRoadPieceLocations: CatanJacksonModelResponse
        <MutableMap<CatanRoadCoordinates, CatanColor?>,
                String,
                List<RoadLocationsModel>>() {

    /*
    * eg sample output:
    *
    * "roads": {
    *   "red": [[(0,1), (0, 2)], ...]
    *
    * }
    *
    * */
    override fun createJacksonFriendlyModel(model: MutableMap<CatanRoadCoordinates, CatanColor?>):
            HashMap<String, List<RoadLocationsModel>> {
        return hashMapOf(
            "red" to getRoadPieces(model, CatanColor.RED),
            "blue" to getRoadPieces(model, CatanColor.BLUE),
            "white" to getRoadPieces(model, CatanColor.WHITE),
            "yellow" to getRoadPieces(model, CatanColor.YELLOW)
        )
    }

    private fun getRoadPieces(model: MutableMap<CatanRoadCoordinates, CatanColor?>, ofColor: CatanColor): List<RoadLocationsModel> {
        return model.keys.filter { key -> model[key] == ofColor }
            .map { key -> hashMapOf(
                "x" to key.roadCoordinates.first().x,
                "y" to key.roadCoordinates.first().y,
                "x1" to key.roadCoordinates.last().x,
                "y1" to key.roadCoordinates.last().y
            ) }
    }
}