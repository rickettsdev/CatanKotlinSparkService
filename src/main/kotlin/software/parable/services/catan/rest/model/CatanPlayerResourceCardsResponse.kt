package software.parable.services.catan.rest.model

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanResource

class CatanPlayerResourceCardsResponse: CatanJacksonModelResponse<HashMap<CatanColor, MutableList<CatanResource>>, String, List<String>>() {
    override fun createJacksonFriendlyModel(model: HashMap<CatanColor, MutableList<CatanResource>>): HashMap<String, List<String>> {
        return hashMapOf(
            "red" to model[CatanColor.RED]!!.map { catanResource -> catanResource.toString() },
            "blue" to model[CatanColor.BLUE]!!.map { catanResource -> catanResource.toString() },
//            "yellow" to model[CatanColor.YELLOW]!!.map { catanResource -> catanResource.toString() },
//            "white" to model[CatanColor.WHITE]!!.map { catanResource -> catanResource.toString() },
        )
    }
}